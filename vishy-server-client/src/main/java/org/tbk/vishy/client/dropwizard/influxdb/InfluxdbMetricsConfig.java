package org.tbk.vishy.client.dropwizard.influxdb;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import lombok.extern.slf4j.Slf4j;
import metrics_influxdb.InfluxdbHttp;
import metrics_influxdb.InfluxdbReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@ConditionalOnBean(MetricRegistry.class)
@ConditionalOnProperty("vishy.influxdb.enabled")
@EnableConfigurationProperties(InfluxdbProperties.class)
public class InfluxdbMetricsConfig {


    @Autowired
    private InfluxdbProperties properties;

    @Bean
    public InfluxdbHttp influxdbHttp() throws Exception {
        log.info("prepare influxdb reporter: {}:{}", properties.getHost(), properties.getPort());

        InfluxdbHttp influxdbHttp = new InfluxdbHttp(
                properties.getHost(),
                Integer.valueOf(properties.getPort()),
                properties.getDatabase(),
                properties.getUsername(),
                properties.getPassword()
        );

        return influxdbHttp;
    }

    @Bean
    public InfluxdbReporter influxdbReporter(MetricRegistry metricRegistry, InfluxdbHttp influxdb) {
        InfluxdbReporter influxdbReporter = InfluxdbReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .skipIdleMetrics(properties.isSkipIdleMetrics())
                .build(influxdb);

        influxdbReporter.start(properties.getIntervalInSeconds(), TimeUnit.SECONDS);

        return influxdbReporter;
    }
}
