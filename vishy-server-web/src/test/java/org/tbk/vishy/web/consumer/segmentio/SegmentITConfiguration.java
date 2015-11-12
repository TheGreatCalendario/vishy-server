package org.tbk.vishy.web.consumer.segmentio;

import com.github.theborakompanioni.openmrc.OpenMrcRequestConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.tbk.vishy.client.segmentio.SegmentIoConfig;
import org.tbk.vishy.client.segmentio.SegmentIoProperties;

import static org.mockito.Mockito.spy;

@Configuration
@EnableConfigurationProperties(SegmentIoProperties.class)
@ConditionalOnProperty("vishy.segmentio.enabled")
class SegmentITConfiguration extends SegmentIoConfig {

    @Override
    @Bean
    @Primary
    public OpenMrcRequestConsumer analyticsClient() {
        return spy(super.analyticsClient());
    }
}
