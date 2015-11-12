package org.tbk.vishy.client.elasticsearch;

import com.github.theborakompanioni.openmrc.OpenMrc;
import com.github.theborakompanioni.openmrc.mother.InitialRequests;
import com.google.common.collect.Iterables;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tbk.vishy.client.elasticsearch.repository.RequestDocument;
import org.tbk.vishy.client.elasticsearch.repository.RequestElasticRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestApplictaion.class, VishyElasticsearchConfig.class})
@WebIntegrationTest({"server.port:0",
        "vishy.elasticsearch.enabled:true"
        })
public class ElasticsearchOpenMrcClientAdapterIT {

    @Autowired
    private ElasticsearchOpenMrcClientAdapter adapter;

    @Autowired
    private RequestElasticRepository repository;

    @Before
    public void setup() {
        repository.deleteAll();
    }

    @After
    public void teardown() {
        repository.deleteAll();
    }

    @Test
    public void itShouldSaveAValidRequest() {
        OpenMrc.Request request = InitialRequests.protobuf().standardGenericRequest().build();

        adapter.accept(request);

        final Iterable<RequestDocument> all = repository.findAll();

        assertThat(Iterables.size(all), is(1));
    }

}