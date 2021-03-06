package org.tbk.vishy.web.consumer.dropwizward;

import com.github.theborakompanioni.openmrc.OpenMrc;
import com.github.theborakompanioni.openmrc.mother.json.InitialRequestJsonMother;
import com.google.common.net.HttpHeaders;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.tbk.vishy.VishyServerConfiguration;
import org.tbk.vishy.client.dropwizard.DropwizardMetricsClientAdapter;
import org.tbk.vishy.client.keenio.KeenIoProperties;

import java.nio.charset.Charset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {
        VishyServerConfiguration.class, MetricsITConfiguration.class})
@WebIntegrationTest("server.port:0")
public class MetricsIT {
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private DropwizardMetricsClientAdapter clientAdapter;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void itShouldHaveAnKeenOpenMrcClientAdapter() throws Exception {
        assertThat(clientAdapter, is(notNullValue()));
    }

    @Test
    public void itShouldAcceptValidInitialRequests() throws Exception {
        String requestJson = new InitialRequestJsonMother().standardInitialRequest();
        send(requestJson).andExpect(status().isAccepted());

        ArgumentCaptor<OpenMrc.Request> captor = ArgumentCaptor.forClass(OpenMrc.Request.class);
        verify(clientAdapter, times(1)).accept(captor.capture());

        final OpenMrc.Request request = captor.getValue();
        assertThat(request, is(notNullValue()));
        assertThat(request.getType(), is(OpenMrc.RequestType.INITIAL));
    }

    private ResultActions send(String json) throws Exception {
        return mockMvc.perform(post("/openmrc/consume")
                .header(HttpHeaders.USER_AGENT, "Firefox")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(contentType)
                .content(json));
    }
}
