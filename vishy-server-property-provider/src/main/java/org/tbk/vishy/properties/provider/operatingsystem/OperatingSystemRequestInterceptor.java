package org.tbk.vishy.properties.provider.operatingsystem;

import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import com.github.theborakompanioni.openmrc.OpenMrcExtensions;
import com.github.theborakompanioni.openmrc.impl.ExtensionHttpRequestInterceptorSupport;
import org.tbk.vishy.utils.ExtractUserAgent;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by void on 20.06.15.
 */
public class OperatingSystemRequestInterceptor extends ExtensionHttpRequestInterceptorSupport<OpenMrcExtensions.OperatingSystem> {

    private static final OpenMrcExtensions.OperatingSystem UNKNOWN = OpenMrcExtensions.OperatingSystem.newBuilder()
            .setName("?")
            .setManufacturer("?")
            .setGroupName("?")
            .build();

    private static final Function<OperatingSystem, OpenMrcExtensions.OperatingSystem> TO_PROTO = (operatingSystem) ->
            OpenMrcExtensions.OperatingSystem.newBuilder()
                    .setName(operatingSystem.getName())
                    .setManufacturer(operatingSystem.getGroup().getManufacturer().getName())
                    .setGroupName(operatingSystem.getGroup().getName()) // groupName
                    .build();

    public OperatingSystemRequestInterceptor() {
        this(Optional.of(UNKNOWN));
    }

    public OperatingSystemRequestInterceptor(Optional<OpenMrcExtensions.OperatingSystem> defaultValue) {
        super(OpenMrcExtensions.OperatingSystem.operatingSystem, Objects.requireNonNull(defaultValue));
    }

    @Override
    protected Optional<OpenMrcExtensions.OperatingSystem> extract(HttpServletRequest httpServletRequest) {
        return Optional.ofNullable(httpServletRequest)
                .map(ExtractUserAgent.fromHttpRequest)
                .flatMap(Supplier::get)
                .map(UserAgent::getOperatingSystem)
                .map(TO_PROTO);
    }

}
