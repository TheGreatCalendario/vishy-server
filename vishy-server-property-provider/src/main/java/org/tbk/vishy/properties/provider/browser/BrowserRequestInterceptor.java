package org.tbk.vishy.properties.provider.browser;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.Version;
import com.github.theborakompanioni.openmrc.OpenMrcExtensions;
import com.github.theborakompanioni.openmrc.impl.ExtensionHttpRequestInterceptorSupport;
import org.tbk.vishy.utils.ExtractUserAgent;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Created by void on 20.06.15.
 */
public class BrowserRequestInterceptor extends ExtensionHttpRequestInterceptorSupport<OpenMrcExtensions.Browser> {

    private static final Version UNKNOWN_VERSION = new Version("?", "?", "?");
    private static final OpenMrcExtensions.Browser UNKNOWN = OpenMrcExtensions.Browser.newBuilder()
            .setName("?")
            .setType("?")
            .setVersion(UNKNOWN_VERSION.getVersion())
            .setMajorVersion(UNKNOWN_VERSION.getMajorVersion())
            .setManufacturer("?")
            .build();

    private static final BiFunction<Browser, Version, OpenMrcExtensions.Browser> TO_PROTO = (browser, version) ->
            OpenMrcExtensions.Browser.newBuilder()
                    .setName(Strings.nullToEmpty(browser.getGroup().getName()))
                    .setType(Strings.nullToEmpty(browser.getGroup().getBrowserType().getName()))
                    .setVersion(Strings.nullToEmpty(version.getVersion()))
                    .setMajorVersion(Strings.nullToEmpty(version.getVersion()))
                    .setManufacturer(Strings.nullToEmpty(browser.getGroup().getManufacturer().getName()))
                    .build();

    public BrowserRequestInterceptor() {
        this(Optional.of(UNKNOWN));
    }

    public BrowserRequestInterceptor(Optional<OpenMrcExtensions.Browser> defaultValue) {
        super(OpenMrcExtensions.Browser.browser, Objects.requireNonNull(defaultValue));
    }

    @Override
    protected Optional<OpenMrcExtensions.Browser> extract(HttpServletRequest httpServletRequest) {
        return Optional.ofNullable(httpServletRequest)
                .map(ExtractUserAgent.fromHttpRequest)
                .flatMap(Supplier::get)
                .map(ua -> TO_PROTO.apply(ua.getBrowser(), MoreObjects.firstNonNull(ua.getBrowserVersion(), UNKNOWN_VERSION)));
    }

}
