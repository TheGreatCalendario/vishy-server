package org.tbk.vishy.utils.ip;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.net.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

public final class IpUtils {
    private static final Splitter COMA_SPLITTER = Splitter.on(',')
        .omitEmptyStrings()
        .trimResults();

    private IpUtils() {
        throw new UnsupportedOperationException();
    }

    public static Optional<String> getIpAddress(HttpServletRequest request) {
        Optional<String> optionalRemoteAddr = Optional.ofNullable(
            Strings.emptyToNull(
                Collections.list(request.getHeaderNames()).stream()
                    .filter(HttpHeaders.X_FORWARDED_FOR::equalsIgnoreCase)
                    .map(headerName -> request.getHeader(headerName))
                    .map(headerValue -> COMA_SPLITTER.split(headerValue))
                    .flatMap(headerValues -> Lists.newArrayList(headerValues).stream())
                    .findFirst().orElse(request.getRemoteAddr())));

        return optionalRemoteAddr;
    }
}
