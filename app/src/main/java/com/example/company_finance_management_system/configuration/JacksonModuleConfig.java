package com.example.company_finance_management_system.configuration;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class JacksonModuleConfig {

    @Bean
    public JavaTimeModule javaTimeModule() {

        JavaTimeModule module = new JavaTimeModule();

        module.addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE);
        module.addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE);

        return module;

    }
}
