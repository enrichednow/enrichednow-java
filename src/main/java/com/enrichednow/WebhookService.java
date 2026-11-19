package com.enrichednow;

import com.enrichednow.models.WebhookDelivery;
import com.enrichednow.models.WebhookEndpoint;
import com.enrichednow.models.WebhookEndpointList;
import java.util.List;

/**
 * Webhooks: the endpoints told when a run ends, their signing secrets, and the delivery log. Reached through
 * {@link EnrichedNowClient#webhooks()}. Verifying a request that arrives is {@link Webhooks#verify}.
 */
public final class WebhookService {
    private final Transport transport;
    private final Requests requests;

    WebhookService(Transport transport, Requests requests) {
        this.transport = transport;
        this.requests = requests;
    }

    /**
     * The account's endpoints: {@code GET /v1/webhooks}.
     *
     * @return the endpoints, and whether the account has a run secret
     */
    public WebhookEndpointList list() {
        return transport.execute(requests.webhookEndpoints());
    }

    /**
     * Adds an endpoint: {@code POST /v1/webhooks}. The answer is the only time its signing secret
     * ({@link WebhookEndpoint#secret()}) is shown. It is not sent again after a failure that may have reached the API,
     * so that one call never makes two endpoints.
     *
     * @param url         the https URL to send events to
     * @param events      the events it is told about, from {@link WebhookEndpoint#EVENTS}
     * @param description your note on it, or null
     * @return the endpoint, with its secret
     */
    public WebhookEndpoint create(String url, List<String> events, String description) {
        return transport.execute(requests.createWebhook(url, events, description));
    }

    /**
     * One endpoint: {@code GET /v1/webhooks/{id}}.
     *
     * @param endpointId the endpoint's id
     * @return the endpoint
     */
    public WebhookEndpoint get(String endpointId) {
        return transport.execute(requests.webhook(endpointId));
    }

    /**
     * Changes an endpoint: {@code PATCH /v1/webhooks/{id}}.
     *
     * @param endpointId the endpoint's id
     * @param change     what to change
     * @return the endpoint as it is now
     */
    public WebhookEndpoint update(String endpointId, WebhookChange change) {
        return transport.execute(requests.updateWebhook(endpointId, change));
    }

    /**
     * Removes an endpoint: {@code DELETE /v1/webhooks/{id}}. Its deliveries stay in the log.
     *
     * @param endpointId the endpoint's id
     */
    public void delete(String endpointId) {
        transport.execute(requests.deleteWebhook(endpointId));
    }

    /**
     * Gives an endpoint a new signing secret, shown in the answer, and ends the old one.
     *
     * @param endpointId the endpoint's id
     * @return the endpoint, with its new secret
     */
    public WebhookEndpoint rotateSecret(String endpointId) {
        return transport.execute(requests.rotateWebhookSecret(endpointId));
    }

    /**
     * Makes (or replaces) the account's secret for the {@code webhook_url} of a single run: shown once.
     *
     * @return the secret, {@code whsec_…}
     */
    public String createRunSecret() {
        return transport.execute(requests.createRunSecret());
    }

    /**
     * The delivery log, newest first: {@code GET /v1/webhooks/deliveries}.
     *
     * @param params the filter and the page size
     * @return the deliveries, fetched a page at a time as they are iterated
     */
    public PagedIterable<WebhookDelivery> deliveries(WebhookDeliveryListParams params) {
        return new PagedIterable<>(() -> deliveriesPage(params));
    }

    /**
     * One page of the delivery log.
     *
     * @param params the filter, the page size and, to continue an earlier list, the cursor
     * @return the page
     */
    public Page<WebhookDelivery> deliveriesPage(WebhookDeliveryListParams params) {
        return transport.execute(requests.deliveries(params, null));
    }

    /**
     * Sends a delivery's event again, as a new delivery with the same event id.
     *
     * @param deliveryId the delivery's id
     * @return the new delivery
     */
    public WebhookDelivery resend(String deliveryId) {
        return transport.execute(requests.resendDelivery(deliveryId));
    }
}
