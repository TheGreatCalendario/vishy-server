package org.tbk.vishy.hystrix;

import com.github.theborakompanioni.openmrc.OpenMrc;
import com.github.theborakompanioni.openmrc.OpenMrcRequestConsumer;
import com.github.theborakompanioni.openmrc.mapper.OpenMrcHttpRequestMapper;
import com.github.theborakompanioni.openmrc.web.OpenMrcHttpRequestService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class HystrixVishyOpenMrcHttpRequestService extends OpenMrcHttpRequestService {
    private final static HystrixCommandGroupKey DEFAULT_KEY = HystrixCommandGroupKey.Factory.asKey("ProcessOpenMrcRequest");

    private final List<OpenMrcRequestConsumer> requestConsumer;
    private final HystrixCommand.Setter setter;

    public HystrixVishyOpenMrcHttpRequestService(OpenMrcHttpRequestMapper mapper, List<OpenMrcRequestConsumer> requestConsumer) {
        super(mapper, requestConsumer);
        this.requestConsumer = requireNonNull(requestConsumer);

        this.setter = HystrixCommand.Setter.withGroupKey(DEFAULT_KEY).andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                .withRequestLogEnabled(false)
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                .withFallbackIsolationSemaphoreMaxConcurrentRequests(1_000)
                .withExecutionTimeoutInMilliseconds(5_000));
    }

    @Override
    public OpenMrc.Response.Builder processRequest(OpenMrc.Request request) {
        requestConsumer.stream()
                .map(consumer -> new ProcessOpenMrcRequestCommand(setter, consumer, request))
                .forEach(ProcessOpenMrcRequestCommand::execute);

        return OpenMrc.Response.newBuilder().setId(UUID.randomUUID().toString());
    }

}
