package org.tbk.vishy.properties.provider.geolocation.impl.freegeoip;

import com.netflix.hystrix.HystrixCommand;
import org.tbk.vishy.properties.provider.geolocation.GeoLocation;
import org.tbk.vishy.properties.provider.geolocation.resolver.GeoLocationResolver;

import java.util.Optional;

/**
 * Created by void on 07.08.15.
 */
public class GeoLocationCommand extends HystrixCommand<Optional<GeoLocation>> {

    private Optional<GeoLocation> fallback;
    private final GeoLocationResolver resolver;
    private final String ipAddress;

    public GeoLocationCommand(Setter setter, GeoLocationResolver resolver, String ipAddress) {
        this(setter, Optional.empty(), resolver, ipAddress);
    }

    public GeoLocationCommand(Setter setter, Optional<GeoLocation> fallback, GeoLocationResolver resolver, String ipAddress) {
        super(setter);
        this.fallback = fallback;
        this.resolver = resolver;
        this.ipAddress = ipAddress;
    }

    @Override
    protected Optional<GeoLocation> run() throws Exception {
        return resolver.getLocation(ipAddress);
    }

    @Override
    protected Optional<GeoLocation> getFallback() {
        return fallback;
    }
}
