package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** What saying a row is right or wrong did: the row's status now, and the correction the account's runs now use. */
public final class RowFeedback {
    private final String rowId;
    private final String verdict;
    private final String status;
    private final Correction correction;

    @JsonCreator
    RowFeedback(@JsonProperty("row_id") String rowId,
                @JsonProperty("verdict") String verdict,
                @JsonProperty("status") String status,
                @JsonProperty("correction") Correction correction) {
        this.rowId = rowId;
        this.verdict = verdict;
        this.status = status;
        this.correction = correction;
    }

    /** @return the row */
    public String rowId() {
        return rowId;
    }

    /** @return the verdict given: {@code good}, {@code wrong_company}, {@code wrong_contact} or {@code not_relevant} */
    public String verdict() {
        return verdict;
    }

    /** @return the row's status now: {@code new}, {@code flagged} or {@code rejected} */
    public String status() {
        return status;
    }

    /** @return with {@code wrong_company}, the correction this account's runs now use; otherwise null */
    public Correction correction() {
        return correction;
    }

    @Override
    public String toString() {
        return "RowFeedback{rowId=" + rowId + ", verdict=" + verdict + ", status=" + status + ", correction=" + correction + '}';
    }

    /** How the account's runs now match a company that a row had wrong. */
    public static final class Correction {
        private final String companyName;
        private final String country;
        private final String domain;

        @JsonCreator
        Correction(@JsonProperty("company_name") String companyName,
                   @JsonProperty("country") String country,
                   @JsonProperty("domain") String domain) {
            this.companyName = companyName;
            this.country = country;
            this.domain = domain;
        }

        /** @return the company's name, as the row had it */
        public String companyName() {
            return companyName;
        }

        /** @return its country, or null */
        public String country() {
            return country;
        }

        /** @return the domain the account's runs now match it to; null: left unmatched */
        public String domain() {
            return domain;
        }

        @Override
        public String toString() {
            return "Correction{companyName=" + companyName + ", country=" + country + ", domain=" + domain + '}';
        }
    }
}
