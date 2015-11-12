package org.tbk.vishy.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.googlecode.protobuf.format.JsonFormat;
import com.github.theborakompanioni.openmrc.OpenMrc;

import java.io.IOException;
import java.util.Map;

/**
 * Created by void on 21.06.15.
 */
public class RequestToMapFunction implements OpenMrcRequestToMapFunction {

    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();

    private final ObjectMapper mapper;

    public RequestToMapFunction() {
        this(DEFAULT_MAPPER);
    }
    public RequestToMapFunction(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Map<String, Object> apply(OpenMrc.Request request) {
        String json = JsonFormat.printToString(request);
        try {
            return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
