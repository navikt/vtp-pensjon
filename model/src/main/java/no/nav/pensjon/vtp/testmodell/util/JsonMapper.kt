package no.nav.pensjon.vtp.testmodell.util

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.annotation.PropertyAccessor.*
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.DeserializationFeature.*
import com.fasterxml.jackson.databind.SerializationFeature.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import java.time.LocalDate
import java.time.LocalDate.parse
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.*

/** NB - Single-use only. Variable lest/skrevet caches internt i modul. Variable som brukes vil deles på tvers av invokeringer. */
class JsonMapper constructor(vars: VariabelContainer = VariabelContainer()) {
    companion object {
        private val OBJECT_MAPPER: ObjectMapper

        fun parseLocalDateOrNull(date: String?): LocalDate? {
            if (date == null) {
                return null
            }

            when {
                date.matches("\\d{4}".toRegex()) -> {
                    return parse(
                        date,
                        DateTimeFormatterBuilder()
                            .appendPattern("uuuu")
                            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                            .toFormatter()
                    )
                }
                date.matches("\\d{4}-\\d{2}".toRegex()) -> {
                    return parse(
                        date,
                        DateTimeFormatterBuilder()
                            .appendPattern("uuuu-MM")
                            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                            .toFormatter()
                    )
                }
                date.matches("\\d{4}-\\d{2}-\\d{2}".toRegex()) -> {
                    return parse(
                        date,
                        DateTimeFormatterBuilder()
                            .appendPattern("uuuu-MM-dd")
                            .toFormatter()
                    )
                }
                else -> {
                    return null
                }
            }
        }

        init {
            val birthdateModule = SimpleModule().apply {
                addDeserializer(
                    LocalDate::class.java,
                    object : JsonDeserializer<LocalDate?>() {
                        override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): LocalDate? {
                            return parseLocalDateOrNull(jp.readValueAs(String::class.java))
                        }
                    }
                )
            }

            OBJECT_MAPPER = ObjectMapper().apply {
                registerModule(Jdk8Module())
                registerModule(ParameterNamesModule())
                setVisibility(GETTER, NONE)
                setVisibility(SETTER, NONE)
                setVisibility(FIELD, NONE)
                setVisibility(CREATOR, ANY)
                configure(FAIL_ON_UNKNOWN_PROPERTIES, true)
                disable(WRITE_DATES_AS_TIMESTAMPS)
                setSerializationInclusion(NON_EMPTY)
                registerModule(birthdateModule)
            }
        }
    }

    val vars: VariabelContainer
    private val injectableValues = InjectableValues.Std()
    fun addVars(vars: VariabelContainer) {
        this.vars.putAll(vars)
    }

    fun lagObjectMapper(): ObjectMapper {
        val objectMapper = OBJECT_MAPPER.copy()
        objectMapper.registerModule(DeserializerModule(vars))
        objectMapper.injectableValues = injectableValues
        return objectMapper
    }

    fun addVars(vars2: Map<String, String>) {
        vars.putAll(vars2)
    }

    init {
        Objects.requireNonNull(vars, "vars")
        this.vars = vars
        injectableValues.addValue(VariabelContainer::class.java, vars)
    }
}
