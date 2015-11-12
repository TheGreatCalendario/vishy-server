package org.tbk.vishy.utils.ip;

import com.google.common.net.HttpHeaders;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by void on 21.06.15.
 */
public class IpUtilsTest {

    @Test
    public void itShouldExtractLoopbackIpAddress() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Optional<String> ipAddress = IpUtils.getIpAddress(request);

        assertThat("ip address is not null", ipAddress, is(notNullValue()));
        assertThat("ip address is present", ipAddress.isPresent(), is(true));
        assertThat("ip address equals loopback address", ipAddress.get(), equalTo("127.0.0.1"));
    }

    @Test
    public void itShouldExtractGivenIpAddress() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("192.168.0.1");

        Optional<String> ipAddress = IpUtils.getIpAddress(request);

        assertThat("ip address is not null", ipAddress, is(notNullValue()));
        assertThat("ip address is present", ipAddress.isPresent(), is(true));
        assertThat("ip address equals given address", ipAddress.get(), equalTo("192.168.0.1"));
    }

    @Test
    public void itShouldExtractIpAddressFromForwardedForHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("192.168.0.1");
        request.addHeader(HttpHeaders.X_FORWARDED_FOR, "192.168.0.2, 192.168.0.3, 192.168.0.4");

        Optional<String> ipAddress = IpUtils.getIpAddress(request);

        assertThat("ip address is not null", ipAddress, is(notNullValue()));
        assertThat("ip address is present", ipAddress.isPresent(), is(true));
        assertThat("ip address equals first X_FORWARDEDE_FOR header address", ipAddress.get(), equalTo("192.168.0.2"));
    }
}