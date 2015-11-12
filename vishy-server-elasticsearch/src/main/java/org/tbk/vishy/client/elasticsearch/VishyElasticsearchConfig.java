package org.tbk.vishy.client.elasticsearch;

import com.github.theborakompanioni.openmrc.OpenMrcRequestConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.tbk.vishy.client.elasticsearch.repository.RequestElasticRepository;
import org.tbk.vishy.client.elasticsearch.repository._package;

@Slf4j
@Configuration
@EnableConfigurationProperties(VishyElasticsearchProperties.class)
@ConditionalOnProperty("vishy.elasticsearch.enabled")
@EnableElasticsearchRepositories(basePackageClasses = _package.class)
public class VishyElasticsearchConfig {

    @Autowired
    private Environment environment;

    @Autowired
    private VishyElasticsearchProperties properties;

    @Bean
    public OpenMrcRequestConsumer elasticsearchOpenMrcClientAdapter(RequestElasticRepository template) {
        return new ElasticsearchOpenMrcClientAdapter(template);
    }

}
