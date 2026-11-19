package com.enrichednow.models;

import java.util.List;

/** The account's webhook endpoints, and whether it has a secret for a single run's {@code webhook_url}. */
public final class WebhookEndpointList {
    private final List<WebhookEndpoint> endpoints;
    private final boolean runSecretSet;

    /**
     * @param endpoints    the endpoints
     * @param runSecretSet whether the account has a run secret
     */
    public WebhookEndpointList(List<WebhookEndpoint> endpoints, boolean runSecretSet) {
        this.endpoints = Values.list(endpoints);
        this.runSecretSet = runSecretSet;
    }

    /** @return the endpoints */
    public List<WebhookEndpoint> endpoints() {
        return endpoints;
    }

    /** @return whether the account has a secret for the {@code webhook_url} of a single run */
    public boolean runSecretSet() {
        return runSecretSet;
    }

    @Override
    public String toString() {
        return "WebhookEndpointList{endpoints=" + endpoints + ", runSecretSet=" + runSecretSet + '}';
    }
}
