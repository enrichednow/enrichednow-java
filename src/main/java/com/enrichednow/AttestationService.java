package com.enrichednow;

import com.enrichednow.models.Attestation;
import java.util.List;

/**
 * The terms some formulas need accepted before they run: those that return people's contact details. Reached through
 * {@link EnrichedNowClient#attestations()}.
 *
 * <p>An owner or an admin of the account accepts them, in the console on the Formulas page; an API key cannot. Until
 * they are accepted, starting such a formula throws an {@link com.enrichednow.errors.AttestationRequiredException}.
 */
public final class AttestationService {
    private final Transport transport;
    private final Requests requests;

    AttestationService(Transport transport, Requests requests) {
        this.transport = transport;
        this.requests = requests;
    }

    /**
     * Each kind of terms, its current text, and whether the account accepted that version: {@code GET /v1/attestations}.
     *
     * @return the terms
     */
    public List<Attestation> list() {
        return transport.execute(requests.attestations());
    }
}
