package org.tbk.vishy.client;

import org.tbk.openmrc.OpenMrc;

import java.util.Map;
import java.util.function.Function;

/**
 * Created by void on 21.06.15.
 */
public interface OpenMrcRequestToMapFunction extends Function<OpenMrc.Request, Map<String, Object>> {
}
