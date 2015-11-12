package org.tbk.vishy.config;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public Jdk8Module jacksonJdk8Module() {
        return new Jdk8Module();
    }


}
