package org.tbk.vishy.properties.provider.geolocation.impl.freegeoip.resolver;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.tbk.vishy.properties.provider.geolocation.GeoLocation;
import org.tbk.vishy.properties.provider.geolocation.impl.freegeoip.FreeGeoIpLocation;
import org.tbk.vishy.properties.provider.geolocation.resolver.GeoLocationResolver;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by void on 02.05.15.
 */
@Slf4j
public class FreeGeoIpLocationResolver implements GeoLocationResolver {
    private static final String REMOTE_URL_PATTERN = "http://freegeoip.net/json/{ip}";

    private final LoadingCache<String, Optional<GeoLocation>> locationCache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.DAYS)
            .maximumSize(10_000)
            .build(CacheLoader.from((ip) -> {
                try {
                    Optional<GeoLocation> optionalLocation = fetchLocation(ip);

                    log.debug("Resolved ip {} to location {}", ip, optionalLocation.orElse(null));

                    return optionalLocation;
                } catch (UnirestException e) {
                    log.warn("Could not resolve location from ip {}: {}", ip, e.getMessage());
                    return Optional.empty();
                }
            }));

    @Override
    public Optional<GeoLocation> getLocation(String ipAddress) {
        try {
            return locationCache.get(ipAddress);
        } catch (ExecutionException e) {
            return Optional.empty();
        }
    }

    private Optional<GeoLocation> fetchLocation(String ip) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(REMOTE_URL_PATTERN)
                .header("accept", "application/json")
                .routeParam("ip", ip)
                .asJson();

        return response.getStatus() == HttpStatus.OK.value() ?
                Optional.ofNullable(convert(response.getBody().getObject())) :
                Optional.empty();
    }

    /**
     * Example Response:
     * {
     * "ip": "192.30.252.131",
     * "country_code": "US",
     * "country_name": "United States",
     * "region_code": "CA",
     * "region_name": "California",
     * "city": "San Francisco",
     * "zip_code": "94107",
     * "time_zone": "America/Los_Angeles",
     * "latitude":37.77,
     * "longitude":-122.394,
     * "metro_code":807
     * }
     *
     * @param json
     * @return
     */
    private GeoLocation convert(JSONObject json) {
        Objects.requireNonNull(json);

        return new FreeGeoIpLocation.Builder()
                .setLat(json.getDouble("latitude"))
                .setLng(json.getDouble("longitude"))
                .setCountry(json.getString("country_name"))
                .setCountryCode(json.getString("country_code"))
                .setRegion(json.getString("region_name"))
                .setRegionCode(json.getString("region_code"))
                .setCity(json.getString("city"))
                .setZipCode(json.getString("zip_code"))
                .setTimezone(json.getString("time_zone"))
                .build();
    }
}
