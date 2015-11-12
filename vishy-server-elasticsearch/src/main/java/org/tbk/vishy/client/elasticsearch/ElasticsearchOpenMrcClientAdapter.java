package org.tbk.vishy.client.elasticsearch;

import com.github.theborakompanioni.openmrc.OpenMrc;
import com.github.theborakompanioni.openmrc.OpenMrcRequestConsumer;
import lombok.extern.slf4j.Slf4j;
import org.tbk.vishy.client.RequestToMapFunction;
import org.tbk.vishy.client.elasticsearch.repository.RequestDocument;
import org.tbk.vishy.client.elasticsearch.repository.RequestElasticRepository;

/**
 * Created by void on 01.05.15.
 */
@Slf4j
public class ElasticsearchOpenMrcClientAdapter implements OpenMrcRequestConsumer {

    private RequestElasticRepository template;

    public ElasticsearchOpenMrcClientAdapter(RequestElasticRepository template) {
        this.template = template;
    }

    @Override
    public void accept(OpenMrc.Request request) {
        RequestDocument document = new RequestDocument();
        document.setRequest(new RequestToMapFunction().apply(request));

        final RequestDocument savedDocument = template.save(document);

        log.debug("Request saved {}: {}", savedDocument.getId(), savedDocument.getRequest());
    }
}
