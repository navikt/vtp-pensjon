package no.nav.pensjon.vtp.testmodell.util

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer
import java.io.IOException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.BeanDescription
import org.threeten.extra.PeriodDuration
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.LocalDateTime
import java.util.function.Function
import java.util.regex.Pattern

class DeserializerModule(private val vars: VariabelContainer) :
    SimpleModule("VTP-DESERIALIZER", Version(1, 0, 0, null, null, null)) {
    private fun initTimeVar(basedt: String): LocalDateTime {
        return LocalDateTime.parse(vars.computeIfAbsent(basedt, Function { LocalDateTime.now().toString() }))
    }

    internal inner class LocalDateTimeDeserializer : StdScalarDeserializer<LocalDateTime>(LocalDateTime::class.java) {
        @Throws(IOException::class, JsonProcessingException::class)
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDateTime {
            val text = p.text
            var reformatted = text
            for ((key, value) in vars.getVars()) {
                value?.let { reformatted = reformatted.replace("\${$key}", it) }
            }
            val testRegexp = Pattern.compile("^(now|basedate)\\(\\)\\s*(([+-])\\s*(P[0-9TYMWDHS].*))?$")
            val matcher = testRegexp.matcher(reformatted)
            return if (matcher.matches()) {
                val baseref = matcher.group(1)
                val base = initTimeVar(baseref)
                val op = matcher.group(3)
                if (op != null) {
                    val per = matcher.group(4)
                    val period = PeriodDuration.parse(per)
                    if ("-" == op) {
                        base.minus(period)
                    } else {
                        base.plus(period)
                    }
                } else {
                    base
                }
            } else {
                LocalDateTime.parse(reformatted)
            }
        }
    }

    init {
        addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer())
        setDeserializerModifier(object : BeanDeserializerModifier() {
            override fun modifyDeserializer(
                config: DeserializationConfig,
                beanDesc: BeanDescription,
                deserializer: JsonDeserializer<*>?
            ): JsonDeserializer<*> {
                return if (String::class.java.isAssignableFrom(beanDesc.beanClass)) {
                    StringVarDeserializer(super.modifyDeserializer(config, beanDesc, deserializer), vars)
                } else {
                    super.modifyDeserializer(config, beanDesc, deserializer)
                }
            }
        })
    }
}
