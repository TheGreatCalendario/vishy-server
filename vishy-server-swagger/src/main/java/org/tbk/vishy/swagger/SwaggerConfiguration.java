package org.tbk.vishy.swagger;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(paths())
                .build()
                .apiInfo(apiInfo())
                .enableUrlTemplating(true);
    }

    private Predicate<String> paths() {
        return or(
                //regex("/.*"),
                regex("/openmrc/.*")
        );
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfo(
                "OpenMRC REST API",
                "The OpenMRC REST API interface definition",
                "0.0.1",
                "API Terms Of Service",
                "tbk",
                "MIT License",
                "https://opensource.org/licenses/MIT"
        );
    }
}