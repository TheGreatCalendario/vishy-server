package org.tbk.vishy.client.dropwizard;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("vishy.metrics")
public class DropwizardMetricsProperties {

    private boolean enabled;
    private boolean console;
    private long intervalInSeconds;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isConsole() {
        return console;
    }

    public void setConsole(boolean console) {
        this.console = console;
    }

    public long getIntervalInSeconds() {
        return intervalInSeconds;
    }

    public void setIntervalInSeconds(long intervalInSeconds) {
        this.intervalInSeconds = intervalInSeconds;
    }
}