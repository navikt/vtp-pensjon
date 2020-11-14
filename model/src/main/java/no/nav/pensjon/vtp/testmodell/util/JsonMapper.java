package no.nav.pensjon.vtp.testmodell.util;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** NB - Single-use only. Variable lest/skrevet caches internt i modul. Variable som brukes vil deles på tvers av invokeringer.*/
public class JsonMapper {
    private static final Logger LOG = LoggerFactory.getLogger(JsonMapper.class);


    private static final ObjectMapper OBJECT_MAPPER;
    static {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new ParameterNamesModule());

        objectMapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.SETTER, Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.CREATOR, Visibility.ANY);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        objectMapper.setSerializationInclusion(Include.NON_EMPTY);

        SimpleModule birthdateModule = new SimpleModule();
        birthdateModule.addDeserializer(LocalDate.class, new JsonDeserializer<>() {
            @Override
            public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return parseLocalDate(jp.readValueAs(String.class));
            }
        });
        objectMapper.registerModule(birthdateModule);
        OBJECT_MAPPER = objectMapper;
    }

    public static LocalDate parseLocalDate(String date) {
        if (date == null){
            return null;
        }
        if(date.matches("\\d{4}")) {
            return LocalDate.parse(date, new DateTimeFormatterBuilder()
                    .appendPattern("uuuu")
                    .parseDefaulting(MONTH_OF_YEAR, 1)
                    .parseDefaulting(DAY_OF_MONTH, 1)
                    .toFormatter());
        }
        else if(date.matches("\\d{4}-\\d{2}")) {
            return LocalDate.parse(date, new DateTimeFormatterBuilder()
                    .appendPattern("uuuu-MM")
                    .parseDefaulting(DAY_OF_MONTH, 1)
                    .toFormatter());
        }
        else if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return LocalDate.parse(date, new DateTimeFormatterBuilder()
                    .appendPattern("uuuu-MM-dd")
                    .toFormatter());
        }
        return null;
    }

    private final VariabelContainer vars;
    private final InjectableValues.Std injectableValues = new InjectableValues.Std();

    public JsonMapper() {
        this(new VariabelContainer());
    }

    public JsonMapper(VariabelContainer vars) {
        Objects.requireNonNull(vars, "vars");
        this.vars = vars;
        this.injectableValues.addValue(VariabelContainer.class, vars);
    }

    public void addVars(VariabelContainer vars) {
        this.vars.putAll(vars);
    }

    public ObjectMapper lagObjectMapper() {
        ObjectMapper objectMapper = OBJECT_MAPPER.copy();
        objectMapper.registerModule(new DeserializerModule(vars));
        objectMapper.setInjectableValues(injectableValues);
        return objectMapper;
    }

    public Set<String> findVariables(Reader... jsonInput) {

        Set<String> vars = new TreeSet<>();
        Pattern varPattern = Pattern.compile("\\$\\{(.+)\\}");
        for (Reader r : jsonInput) {
            try (BufferedReader buffer = new BufferedReader(r)) {
                String text = buffer.lines().collect(Collectors.joining(System.lineSeparator()));
                Matcher m = varPattern.matcher(text);
                while (m.find()) {
                    String varName = m.group(1);
                    vars.add(varName);
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        return Collections.unmodifiableSet(vars);

    }

    public ObjectMapper canonicalMapper() {
        ObjectMapper om = OBJECT_MAPPER.copy();
        om.registerModule(new CanonicalSerializerModule());
        return om;
    }

    public void addInjectable(Class<?> type, Object val) {
        injectableValues.addValue(type, val);
    }

    public void addVars(Map<String, String> vars2) {
        this.vars.putAll(vars2);
    }

    public VariabelContainer getVars() {
        return vars;
    }

}
