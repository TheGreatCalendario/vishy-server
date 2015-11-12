package org.tbk.vishy.properties.provider.geolocation.impl.freegeoip.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tbk.vishy.properties.provider.geolocation.resolver.GeoLocationResolverConfiguration;

import javax.annotation.PostConstruct;

/**
 * Created by void on 08.03.15.
 */
@Slf4j
@Configuration
public class FreeGeoIpLocationResolverConfiguration implements GeoLocationResolverConfiguration {

    @PostConstruct
    public void init() {
        log.info("initialize");
    }

    @Bean
    public FreeGeoIpLocationResolver geoLocationSupplier() {
        return new FreeGeoIpLocationResolver();
    }
}
