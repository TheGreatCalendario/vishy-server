package org.tbk.vishy.utils;

import com.github.theborakompanioni.spring.useragentutils.UserAgentUtils;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by void on 27.05.15.
 */
public final class ExtractUserAgent {
    public static Function<HttpServletRequest, Supplier<Optional<UserAgent>>> fromHttpRequest =
            request -> (Supplier<Optional<UserAgent>>) () -> Optional.ofNullable(UserAgentUtils.getCurrentUserAgent(request));
}
