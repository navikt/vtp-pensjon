package no.nav.pensjon.vtp.testmodell.util

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonStreamContext
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.vtp.testmodell.repo.TemplateVariable
import java.io.IOException
import java.io.Reader
import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.util.*
import java.util.regex.Pattern

/** Scanner input for template variable og finner type, path etc.  */
class FindTemplateVariables {
    private val objectMapper = ObjectMapper()
    private val module = FindTemplateVariableModule()
    val discoveredVariables: Set<TemplateVariable>
        get() = module.getVars()

    fun scanForVariables(targetClass: Class<*>, reader: Reader?) {
        if (reader == null) {
            return
        }
        try {
            // ignore return, fanger opp variabler i #module ved deserialisering
            objectMapper.readValue(reader, targetClass)
        } catch (e: IOException) {
            throw IllegalArgumentException("Kunne ikke deserialisere til " + targetClass.name, e)
        }
    }

    internal class FindTemplateVariableModule : SimpleModule() {
        private val vars: MutableSet<TemplateVariable> = HashSet()
        fun getVars(): Set<TemplateVariable> {
            return Collections.unmodifiableSet(vars)
        }

        init {
            setDeserializerModifier(object : BeanDeserializerModifier() {
                override fun modifyDeserializer(
                    config: DeserializationConfig,
                    beanDesc: BeanDescription,
                    deserializer: JsonDeserializer<*>?
                ): JsonDeserializer<*> {
                    return FindTemplateVariableDeserializer(
                        super.modifyDeserializer(config, beanDesc, deserializer),
                        vars
                    )
                }
            })
        }
    }

    internal class FindTemplateVariableDeserializer(
        private val delegate: JsonDeserializer<*>,
        private val vars: MutableSet<TemplateVariable>
    ) : JsonDeserializer<Any?>(), ContextualDeserializer, ResolvableDeserializer {
        /** glue-code.  */
        @Throws(JsonMappingException::class)
        override fun resolve(ctxt: DeserializationContext) {
            if (delegate is ResolvableDeserializer) {
                (delegate as ResolvableDeserializer).resolve(ctxt)
            }
        }

        /** glue-code.  */
        override fun createContextual(ctxt: DeserializationContext?, property: BeanProperty?): JsonDeserializer<*> {
            var delSer = delegate
            if (delSer is ContextualDeserializer) {
                delSer = (delegate as ContextualDeserializer).createContextual(ctxt, property)
            }
            return if (delSer === delegate) {
                this
            } else FindTemplateVariableDeserializer(delSer, vars)
        }

        /** sjekk om inneholder variabel referanse og ta vare på det.  */
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Any? {
            if (!p.currentToken().isScalarValue) {
                return delegate.deserialize(p, ctxt)
            }
            val text = p.text
            val matcher = TEMPLATE_VARIABLE_PATTERN.matcher(text)
            if (matcher.find()) {
                // antar p.t. kun en variabel per node
                val varName = matcher.group(1)
                val pc = p.parsingContext
                val path = getPath(pc)
                val targetClass = delegate.handledType()
                vars.add(TemplateVariable(targetClass, varName, path, null))
            }
            // returnerer null slik at dette ikke går i beina på senere parsing
            return null
        }
    }

    companion object {
        val TEMPLATE_VARIABLE_PATTERN = Pattern.compile("\\$\\{(.+)\\}")
        fun getPath(streamContext: JsonStreamContext?): String {
            var pc = streamContext
            val sb = StringBuilder(pc!!.currentName)
            pc = pc.parent
            while (pc != null && pc.currentName != null) {
                sb.insert(0, pc.currentName + ".")
                pc = pc.parent
            }
            return sb.toString()
        }
    }

    init {
        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
        objectMapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE)
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE)
        objectMapper.setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.ANY)
        objectMapper.registerModule(module)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.injectableValues = object : InjectableValues.Std() {
            @Throws(JsonMappingException::class)
            override fun findInjectableValue(
                valueId: Any,
                ctxt: DeserializationContext,
                forProperty: BeanProperty,
                beanInstance: Any
            ): Any? {
                // skipper all injection uten å feile
                return null
            }
        }
    }
}
