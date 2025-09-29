package at.technikum.application.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class Json {
    public static final ObjectMapper MAPPER = new ObjectMapper()
            .findAndRegisterModules() // auto-registers JavaTimeModule if on classpath
            .registerModule(new JavaTimeModule()) // ensure Instant is supported
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO-8601 instead of epoch millis

    private Json(){}
}
