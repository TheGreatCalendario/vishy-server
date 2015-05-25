package org.tbk.vishy.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.util.Arrays;

/**
 * Created by void on 24.05.15.
 */
@Configuration
public class StaticsResourcesConfiguration extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {

    private static Log logger = LogFactory.getLog(StaticsResourcesConfiguration.class);

    private static final int CACHE_PERIOD = 1000;

    private static final String[] SERVLET_RESOURCE_LOCATIONS = {"/"};

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/static/", "classpath:/public/", "classpath:/vishy-analytics/"
    };

    private static final String[] RESOURCE_LOCATIONS;

    static {
        RESOURCE_LOCATIONS = new String[CLASSPATH_RESOURCE_LOCATIONS.length
                + SERVLET_RESOURCE_LOCATIONS.length];
        System.arraycopy(SERVLET_RESOURCE_LOCATIONS, 0, RESOURCE_LOCATIONS, 0,
                SERVLET_RESOURCE_LOCATIONS.length);
        System.arraycopy(CLASSPATH_RESOURCE_LOCATIONS, 0, RESOURCE_LOCATIONS,
                SERVLET_RESOURCE_LOCATIONS.length, CLASSPATH_RESOURCE_LOCATIONS.length);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);

        logger.info("Add resource handler for 'static/**' to " + Arrays.toString(RESOURCE_LOCATIONS));

        registry.addResourceHandler("/static/**")
                .addResourceLocations(RESOURCE_LOCATIONS)
                .setCachePeriod(CACHE_PERIOD);

    }
}