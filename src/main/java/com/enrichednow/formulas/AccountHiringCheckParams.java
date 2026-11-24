// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Account Hiring Check ({@code account-hiring-check}): Every account in your CRM flagged hiring
 * or not, with its open roles, departments and the roles you care about.
 *
 * Only {@code accounts} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * AccountHiringCheckParams params = AccountHiringCheckParams.builder()
 *     .accounts("gitlab.com", "cloudflare.com")
 *     .build();
 * }</pre>
 */
public final class AccountHiringCheckParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "account-hiring-check";

    private final Map<String, Object> values;

    private AccountHiringCheckParams(Map<String, Object> values) {
        this.values = Collections.unmodifiableMap(new LinkedHashMap<>(values));
    }

    /**
     * A builder with nothing set.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String formulaId() {
        return FORMULA_ID;
    }

    @Override
    public Map<String, Object> toMap() {
        return values;
    }

    @Override
    public String toString() {
        return "AccountHiringCheckParams" + values;
    }

    /** Builds {@link AccountHiringCheckParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Accounts: Each account's website domain, as in your CRM (acme.com). Upload a CSV for many. 1 to 100
         * items. Required.
         *
         * @param accounts the values; not null
         * @return this builder
         */
        public Builder accounts(List<String> accounts) {
            values.put("accounts", List.copyOf(accounts));
            return this;
        }

        /**
         * Accounts: Each account's website domain, as in your CRM (acme.com). Upload a CSV for many. 1 to 100
         * items. Required.
         *
         * @param accounts the values
         * @return this builder
         */
        public Builder accounts(String... accounts) {
            return accounts(Arrays.asList(accounts));
        }

        /**
         * Roles you care about: Openings whose title holds one of these as a whole word or phrase are counted
         * apart, such as Data Engineer. At most 20 items. Default: [].
         *
         * @param rolesOfInterest the values; not null
         * @return this builder
         */
        public Builder rolesOfInterest(List<String> rolesOfInterest) {
            values.put("roles_of_interest", List.copyOf(rolesOfInterest));
            return this;
        }

        /**
         * Roles you care about: Openings whose title holds one of these as a whole word or phrase are counted
         * apart, such as Data Engineer. At most 20 items. Default: [].
         *
         * @param rolesOfInterest the values
         * @return this builder
         */
        public Builder rolesOfInterest(String... rolesOfInterest) {
            return rolesOfInterest(Arrays.asList(rolesOfInterest));
        }

        /**
         * New within (days): An opening posted in the last this many days counts as new. From 1 to 180. Default:
         * 30.
         *
         * @param newWithinDays the value
         * @return this builder
         */
        public Builder newWithinDays(int newWithinDays) {
            values.put("new_within_days", newWithinDays);
            return this;
        }

        /**
         * Hiring at this many openings: An account with at least this many openings is flagged hiring. At least 1.
         * Default: 1.
         *
         * @param hiringThreshold the value
         * @return this builder
         */
        public Builder hiringThreshold(int hiringThreshold) {
            values.put("hiring_threshold", hiringThreshold);
            return this;
        }

        /**
         * Count only tracking-system openings: Counts only openings an applicant tracking system lists. What a
         * careers page's own text shows is counted apart, as unconfirmed. Default: true.
         *
         * @param verifiedOnly the value
         * @return this builder
         */
        public Builder verifiedOnly(boolean verifiedOnly) {
            values.put("verified_only", verifiedOnly);
            return this;
        }

        /**
         * Most rows: One row per account; the run stops at this many, most active first. From 1 to 5000. Default:
         * 1000.
         *
         * @param maxResults the value
         * @return this builder
         */
        public Builder maxResults(int maxResults) {
            values.put("max_results", maxResults);
            return this;
        }

        /**
         * The parameters, once every required one is set.
         *
         * @return the parameters
         * @throws IllegalStateException when a required parameter is not set
         */
        public AccountHiringCheckParams build() {
            if (!values.containsKey("accounts")) {
                throw new IllegalStateException("AccountHiringCheckParams: accounts is required");
            }
            return new AccountHiringCheckParams(values);
        }
    }
}
