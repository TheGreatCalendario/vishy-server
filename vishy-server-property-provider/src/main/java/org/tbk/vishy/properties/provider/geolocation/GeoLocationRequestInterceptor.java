package org.tbk.vishy.properties.provider.geolocation;

import com.github.theborakompanioni.openmrc.OpenMrcExtensions;
import com.github.theborakompanioni.openmrc.impl.ExtensionHttpRequestInterceptorSupport;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.tbk.vishy.properties.provider.geolocation.impl.freegeoip.GeoLocationCommand;
import org.tbk.vishy.properties.provider.geolocation.resolver.GeoLocationResolver;
import org.tbk.vishy.utils.ip.RemoteAddressExtractor;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by void on 20.06.15.
 */
public class GeoLocationRequestInterceptor extends ExtensionHttpRequestInterceptorSupport<OpenMrcExtensions.GeoLocation> {

    private final static HystrixCommandGroupKey DEFAULT_KEY = HystrixCommandGroupKey.Factory
            .asKey("GeoLocationRequestInterceptor");

    private static final OpenMrcExtensions.GeoLocation UNKNOWN = OpenMrcExtensions.GeoLocation.newBuilder()
            .setLat(0f)
            .setLng(0f)
            .setCountry("?")
            .setDisplayCountry("?")
            .setRegion("?")
            .setDisplayRegion("?")
            .setCity("?")
            .setZipCode("?")
            .setTimezone("?")
            .build();

    private static final Function<GeoLocation, OpenMrcExtensions.GeoLocation> TO_PROTO = (geoLocation) ->
            OpenMrcExtensions.GeoLocation.newBuilder()
                    .setLat((float) geoLocation.getLat())
                    .setLng((float) geoLocation.getLng())
                    .setCountry(geoLocation.getCountryCode())
                    .setDisplayCountry(geoLocation.getCountry())
                    .setRegion(geoLocation.getRegionCode())
                    .setDisplayRegion(geoLocation.getRegion())
                    .setCity(geoLocation.getCity())
                    .setZipCode(geoLocation.getZipCode())
                    .setTimezone(geoLocation.getTimezone())
                    .build();

    private final static String DEFAULT_LOCAL_IP_SUBSTITUTE = "8.8.8.8";

    private final GeoLocationResolver geoLocationResolver;
    private final RemoteAddressExtractor remoteAddressExtractor;
    private final HystrixCommand.Setter setter;

    public GeoLocationRequestInterceptor(GeoLocationResolver geoLocationResolver) {
        this(geoLocationResolver, Optional.of(UNKNOWN));
    }

    public GeoLocationRequestInterceptor(GeoLocationResolver geoLocationResolver, Optional<OpenMrcExtensions.GeoLocation> defaultValue) {
        super(OpenMrcExtensions.GeoLocation.geolocation, Objects.requireNonNull(defaultValue));
        this.geoLocationResolver = geoLocationResolver;
        this.remoteAddressExtractor = RemoteAddressExtractor
                .withLocalSubstitude(DEFAULT_LOCAL_IP_SUBSTITUTE);

        this.setter = HystrixCommand.Setter.withGroupKey(DEFAULT_KEY)
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withRequestLogEnabled(false)
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                        .withFallbackIsolationSemaphoreMaxConcurrentRequests(1_000)
                        .withExecutionTimeoutInMilliseconds(5_000));
    }

    @Override
    protected Optional<OpenMrcExtensions.GeoLocation> extract(HttpServletRequest httpServletRequest) {
        return Optional.ofNullable(httpServletRequest)
                .flatMap(remoteAddressExtractor::getIpAddress)
                .map(ipAddress -> new GeoLocationCommand(setter, geoLocationResolver, ipAddress))
                .flatMap(HystrixCommand::execute)
                .map(TO_PROTO);
    }

}
