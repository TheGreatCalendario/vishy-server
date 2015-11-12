package org.tbk.vishy.web;

import com.github.theborakompanioni.openmrc.mother.InitialRequests;
import com.github.theborakompanioni.openmrc.mother.StatusRequests;
import com.github.theborakompanioni.openmrc.mother.SummaryRequests;
import com.github.theborakompanioni.openmrc.mother.json.InitialRequestJsonMother;
import com.github.theborakompanioni.openmrc.mother.json.StatusRequestJsonMother;
import com.github.theborakompanioni.openmrc.mother.json.SummaryRequestJsonMother;
import com.google.common.net.HttpHeaders;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.tbk.vishy.VishyServerConfiguration;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {VishyServerConfiguration.class})
@WebIntegrationTest("server.port:0")
public class VishyIT {
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testHelloGet() throws Exception {
        mockMvc.perform(get("/hello")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testHelloPost() throws Exception {
        mockMvc.perform(post("/hello")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testWithoutRequestBody() throws Exception {
        mockMvc.perform(post("/openmrc/consume")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testWithEmptyRequestBody() throws Throwable {
        send("").andExpect(status().isBadRequest());
        send("{}").andExpect(status().isBadRequest());
    }

    @Test
    public void testWithRequestBodyWithoutTypeInformation() throws Throwable {
        send("{ test: \"Test\" }").andExpect(status().isBadRequest());
    }

    @Test
    public void testWithInitialEventRequestBody() throws Exception {
        String requestJson = InitialRequests.json().standardInitialRequest();
        send(requestJson).andExpect(status().isAccepted());

        String requestWithExtensionJson = InitialRequests.json().initialRequestWithBrowserExtension();
        send(requestWithExtensionJson).andExpect(status().isAccepted());
    }

    @Test
    public void testWithInitialEventWithBrowserExtensionRequestBody() throws Exception {
        String requestWithExtensionJson = InitialRequests.json().initialRequestWithBrowserExtension();
        send(requestWithExtensionJson).andExpect(status().isAccepted());
    }

    @Test
    public void testWithStatusEventRequestBody() throws Exception {
        String requestJson = StatusRequests.json().standardStatusRequest();
        send(requestJson).andExpect(status().isAccepted());
    }

    @Test
    public void testWithSummaryEventRequestBody() throws Exception {
        String requestJson = SummaryRequests.json().standardSummaryRequest();
        send(requestJson).andExpect(status().isAccepted());
    }

    @Test
    public void itShouldAcceptValidInitialRequests() throws Exception {
        String requestJson = new InitialRequestJsonMother().initialRequestWithBrowserExtension();
        send(requestJson).andExpect(status().isAccepted());
    }

    @Test
    public void itShouldAcceptValidStatusRequests() throws Exception {
        String requestJson = new StatusRequestJsonMother().standardStatusRequest();
        send(requestJson).andExpect(status().isAccepted());
    }

    @Test
    public void itShouldAcceptValidStatusRequestsWithFloatValues() throws Exception {
        String requestJson2 = "{\"type\":\"STATUS\",\"monitorId\":\"9f554854-86a6-43b1-a11b-8a987e50f870\",\"status\":{\"test\":{\"monitorState\":{\"code\":1,\"state\":\"visible\",\"percentage\":0.8,\"fullyvisible\":false,\"visible\":true,\"hidden\":false},\"testConfig\":{\"percentageLimit\":0.5,\"timeLimit\":1000,\"interval\":100},\"timeReport\":{\"timeHidden\":0,\"timeVisible\":1268,\"timeFullyVisible\":0,\"timeRelativeVisible\":1030,\"duration\":1268,\"timeStarted\":1435010244664,\"percentage\":{\"current\":0.6,\"maximum\":0.99,\"minimum\":0.77}}}},\"sessionId\":\"5b9a546a-ee0e-44ba-adf1-4d1757422529\",\"viewport\":{\"width\":960,\"height\":198},\"vishy\":{\"id\":\"42\",\"projectId\":\"myElement\"}}";
        send(requestJson2).andExpect(status().isAccepted());
    }

    @Test
    @Repeat(5)
    public void itShouldAcceptValidSummaryRequests() throws Exception {
        String requestJson = new SummaryRequestJsonMother().standardSummaryRequest();
        send(requestJson).andExpect(status().isAccepted());
    }

    @Test
    @Repeat(5)
    public void itShouldDeclineRequestsWithoutValidStatus() throws Exception {
        String requestJson = "{\"type\":\"STATUS--invalid--\",\"monitorId\":\"8\",\"status\":{\"test\":{\"monitorState\":{\"code\":2,\"state\":\"fullyvisible\",\"percentage\":1,\"previous\":{\"code\":2,\"state\":\"fullyvisible\",\"percentage\":1,\"fullyvisible\":true,\"visible\":true,\"hidden\":false},\"fullyvisible\":true,\"visible\":true,\"hidden\":false},\"testConfig\":{\"percentageLimit\":0.5,\"timeLimit\":1000,\"interval\":100},\"timeReport\":{\"timeHidden\":0,\"timeVisible\":1068,\"timeFullyVisible\":1068,\"timeRelativeVisible\":1068,\"duration\":1068,\"timeStarted\":1435006723958,\"percentage\":{\"current\":1,\"maximum\":1,\"minimum\":1}}}},\"sessionId\":\"557a4811-7e29-4bb2-8f43-66fb3929591b\",\"viewport\":{\"width\":1920,\"height\":372},\"vishy\":{\"id\":\"42\",\"projectId\":\"myElement\"}}";
        send(requestJson).andExpect(status().isBadRequest());
    }

    private ResultActions send(String json) throws Exception {
        return mockMvc.perform(post("/openmrc/consume")
                .header(HttpHeaders.USER_AGENT, "Firefox")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(contentType)
                .content(json));
    }
}
