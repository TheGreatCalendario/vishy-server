package org.tbk.vishy.hystrix;

import com.github.theborakompanioni.openmrc.OpenMrc;
import com.github.theborakompanioni.openmrc.OpenMrcRequestConsumer;
import com.netflix.hystrix.HystrixCommand;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by void on 07.08.15.
 */
@Slf4j
public class ProcessOpenMrcRequestCommand extends HystrixCommand<OpenMrc.Request> {

    private final OpenMrcRequestConsumer consumer;
    private final OpenMrc.Request request;

    public ProcessOpenMrcRequestCommand(Setter setter, OpenMrcRequestConsumer consumer, OpenMrc.Request request) {
        super(setter);
        this.consumer = consumer;
        this.request = request;
    }

    @Override
    protected OpenMrc.Request run() {
        consumer.accept(request);
        return request;
    }

    @Override
    protected OpenMrc.Request getFallback() {
        log.warn("Returning unprocessed request: {}", request);
        return request;
    }
}
