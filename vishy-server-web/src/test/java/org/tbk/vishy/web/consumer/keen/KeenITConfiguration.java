package org.tbk.vishy.web.consumer.keen;

import com.github.theborakompanioni.openmrc.OpenMrc;
import com.github.theborakompanioni.openmrc.OpenMrcRequestConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.tbk.vishy.client.RequestToMapFunction;
import org.tbk.vishy.client.keenio.KeenIoConfig;
import org.tbk.vishy.client.keenio.KeenIoProperties;
import org.tbk.vishy.client.keenio.KeenOpenMrcClientAdapter;

import java.util.function.Function;

import static org.mockito.Mockito.spy;

@Configuration
@EnableConfigurationProperties(KeenIoProperties.class)
@ConditionalOnProperty("vishy.keenio.enabled")
class KeenITConfiguration extends KeenIoConfig {

    @Override
    @Bean
    @Primary
    public OpenMrcRequestConsumer keenOpenMrcClientAdapter() {
        String testEventPrefix = "test_";
        Function<OpenMrc.Request, String> toEventNameFunction = request -> testEventPrefix + request.getType().name();
        final KeenOpenMrcClientAdapter client = new KeenOpenMrcClientAdapter(keenClient(), new RequestToMapFunction(), toEventNameFunction);

        final KeenOpenMrcClientAdapter clientSpy = spy(client);

        return clientSpy;
    }
}
