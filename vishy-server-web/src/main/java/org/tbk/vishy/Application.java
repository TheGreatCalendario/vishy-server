package org.tbk.vishy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.system.ApplicationPidFileWriter;
import org.springframework.boot.actuate.system.EmbeddedServerPortFileWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationListener;

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(Application.class)
                .showBanner(true)
                .listeners(applicationPidFileWriter(), embeddedServerPortFileWriter())
                .web(true)
                .run(args);
    }

    public static ApplicationListener<?> applicationPidFileWriter() {
        return new ApplicationPidFileWriter("app.pid");
    }

    public static ApplicationListener<?> embeddedServerPortFileWriter() {
        return new EmbeddedServerPortFileWriter("app.port");
    }


}