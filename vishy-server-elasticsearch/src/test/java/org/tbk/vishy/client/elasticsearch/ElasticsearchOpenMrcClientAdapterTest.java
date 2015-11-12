package org.tbk.vishy.client.elasticsearch;

import com.github.theborakompanioni.openmrc.OpenMrc;
import com.github.theborakompanioni.openmrc.mother.InitialRequests;
import org.junit.Before;
import org.junit.Test;
import org.tbk.vishy.client.elasticsearch.repository.RequestDocument;
import org.tbk.vishy.client.elasticsearch.repository.RequestElasticRepository;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by void on 11.10.15.
 */
public class ElasticsearchOpenMrcClientAdapterTest {

    private ElasticsearchOpenMrcClientAdapter adapter;
    private RequestElasticRepository repositorySpy;

    @Before
    public void setup() {
        repositorySpy = spy(RequestElasticRepository.class);
        adapter = new ElasticsearchOpenMrcClientAdapter(repositorySpy);
    }

    @Test
    public void itShouldSaveAValidRequest() {
        OpenMrc.Request request = InitialRequests.protobuf().standardGenericRequest().build();
        when(repositorySpy.save(any(RequestDocument.class))).thenReturn(new RequestDocument());

        adapter.accept(request);

        verify(repositorySpy, only()).save(any(RequestDocument.class));
    }

}