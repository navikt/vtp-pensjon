package no.nav.pensjon.vtp.testmodell.util

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

class StringVarDeserializer(private val delegate: JsonDeserializer<*>, private val vars: VariabelContainer) : JsonDeserializer<String>(), ContextualDeserializer, ResolvableDeserializer {
    override fun resolve(ctxt: DeserializationContext) {
        if (delegate is ResolvableDeserializer) {
            (delegate as ResolvableDeserializer).resolve(ctxt)
        }
    }

    override fun createContextual(ctxt: DeserializationContext?, property: BeanProperty?): JsonDeserializer<*> {
        var delSer = delegate
        if (delSer is ContextualDeserializer) {
            delSer = (delegate as ContextualDeserializer).createContextual(ctxt, property)
        }
        return if (delSer === delegate) {
            this
        } else StringVarDeserializer(delSer, vars)
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String {
        val text = p.text
        val matcher = FindTemplateVariables.TEMPLATE_VARIABLE_PATTERN.matcher(text)
        if (!matcher.matches()) {
            return text
        }
        var reformatted = text
        for ((key, value) in vars.getVars()) {
            reformatted = value?.let { reformatted.replace("\${$key}", it) }
        }
        if (reformatted == text) {
            // har ikke funnet deklarasjon for påkrevd variabel.
            vars.computeIfAbsent(matcher.group(1),  java.util.function.Function { n: String -> null })
//            throw IllegalStateException("Mangler variabel deklarasjon for [" + text + "], path=" + FindTemplateVariables.getPath(p.parsingContext))
        }
        return reformatted
    }
}
