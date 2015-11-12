package org.tbk.vishy;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Configuration
@Profile("development")
public class DebugConfiguration {

    @Autowired
    ConfigurableEnvironment environment;

    @Autowired
    ApplicationContext ctx;

    @PostConstruct
    public void init() {
        printBeanDefinitionNames();
        printEnvironmentVariables();
    }

    private void printBeanDefinitionNames() {
        Joiner comaJoiner = Joiner.on(", ");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);

        log.info("Beans in application context: {}", comaJoiner.join(beanNames));
    }

    private void printEnvironmentVariables() {
        Optional.ofNullable(environment)
                .map(ConfigurableEnvironment::getSystemProperties)
                .map(env -> env.entrySet().stream().sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey())))
                .ifPresent(env -> env.forEach(entry -> log.info("{} -> {}", entry.getKey(), entry.getValue())));

        Optional.ofNullable(environment)
                .map(ConfigurableEnvironment::getSystemEnvironment)
                .map(env -> env.entrySet().stream().sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey())))
                .ifPresent(env -> env.forEach(entry -> log.info("{} -> {}", entry.getKey(), entry.getValue())));
    }

}