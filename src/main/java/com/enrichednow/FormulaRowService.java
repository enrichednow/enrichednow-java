package com.enrichednow;

import com.enrichednow.models.RowFeedback;

/**
 * Saying what is right or wrong with a formula's row, so later runs do better. Reached through
 * {@link EnrichedNowClient#formulaRows()}.
 */
public final class FormulaRowService {
    private final Transport transport;
    private final Requests requests;

    FormulaRowService(Transport transport, Requests requests) {
        this.transport = transport;
        this.requests = requests;
    }

    /**
     * Says what is right or wrong with a row: {@code POST /v1/formula-rows/{id}/feedback}. Safe to send again.
     *
     * @param rowId   the row's id, {@code frow_…}
     * @param verdict what is right or wrong with it
     * @return the row's status now
     */
    public RowFeedback feedback(String rowId, RowVerdict verdict) {
        return feedback(rowId, verdict, null, null);
    }

    /**
     * Says what is right or wrong with a row, with a note, and for the wrong company its real domain.
     *
     * @param rowId         the row's id
     * @param verdict       what is right or wrong with it
     * @param note          what you would like known, up to 1000 characters; or null
     * @param correctDomain with {@link RowVerdict#WRONG_COMPANY}, the company's real domain: this account's runs match
     *                      it to that from now on (without one, the company is left unmatched); otherwise null
     * @return the row's status now, and the correction this account's runs now use
     */
    public RowFeedback feedback(String rowId, RowVerdict verdict, String note, String correctDomain) {
        return transport.execute(requests.rowFeedback(rowId, verdict, note, correctDomain));
    }
}
