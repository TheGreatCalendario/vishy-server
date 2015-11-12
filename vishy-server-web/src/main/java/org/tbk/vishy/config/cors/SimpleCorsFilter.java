package org.tbk.vishy.config.cors;

import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


public class SimpleCorsFilter implements Filter {
    private static final CharMatcher BREAKING_MATCHER = CharMatcher.anyOf("\r\n");

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        Optional<String> originHeaderValue = Optional.ofNullable(request.getHeader("Origin"))
                .map(Strings::emptyToNull)
                .map(BREAKING_MATCHER::removeFrom);

        final String allowOrigin = originHeaderValue.orElse("*");
        response.setHeader("Access-Control-Allow-Origin", allowOrigin);
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "accept,content-type,x-requested-with");

        originHeaderValue.ifPresent(foo -> response.setHeader("Access-Control-Allow-Credentials", "true"));

        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }

}
