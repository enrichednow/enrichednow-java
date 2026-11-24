// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Funded and Hiring ({@code funded-and-hiring}): Companies that just raised a round and are
 * hiring on their own careers page, with the founder or talent lead to contact.
 *
 * A value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * FundedAndHiringParams params = FundedAndHiringParams.builder()
 *     .build();
 * }</pre>
 */
public final class FundedAndHiringParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "funded-and-hiring";

    private final Map<String, Object> values;

    private FundedAndHiringParams(Map<String, Object> values) {
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
        return "FundedAndHiringParams" + values;
    }

    /** Builds {@link FundedAndHiringParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Industry words: Keep announcements whose headline or text holds one of these words. Leave empty for any.
         * At most 20 items. Default: [].
         *
         * @param industries the values; not null
         * @return this builder
         */
        public Builder industries(List<String> industries) {
            values.put("industries", List.copyOf(industries));
            return this;
        }

        /**
         * Industry words: Keep announcements whose headline or text holds one of these words. Leave empty for any.
         * At most 20 items. Default: [].
         *
         * @param industries the values
         * @return this builder
         */
        public Builder industries(String... industries) {
            return industries(Arrays.asList(industries));
        }

        /**
         * Announced within (days): Only rounds announced in the last this many days. From 1 to 60. Default: 14.
         *
         * @param publishedWithinDays the value
         * @return this builder
         */
        public Builder publishedWithinDays(int publishedWithinDays) {
            values.put("published_within_days", publishedWithinDays);
            return this;
        }

        /**
         * Least open roles: Openings the company's careers page must list. 0 keeps companies with none. At least 0.
         * Default: 1.
         *
         * @param minOpenRoles the value
         * @return this builder
         */
        public Builder minOpenRoles(int minOpenRoles) {
            values.put("min_open_roles", minOpenRoles);
            return this;
        }

        /**
         * Count openings no tracking system confirms: Many young companies list jobs on a plain careers page. Those
         * count towards the least open roles, and are shown apart. Default: true.
         *
         * @param countUnconfirmed the value
         * @return this builder
         */
        public Builder countUnconfirmed(boolean countUnconfirmed) {
            values.put("count_unconfirmed", countUnconfirmed);
            return this;
        }

        /**
         * Searches: The words each wire is searched for. Each search reads the newest 50 releases that hold them. 1
         * to 8 items. Default: ["raises Series A", "seed funding", "raises Series B", "funding round"].
         *
         * @param searchTerms the values; not null
         * @return this builder
         */
        public Builder searchTerms(List<String> searchTerms) {
            values.put("search_terms", List.copyOf(searchTerms));
            return this;
        }

        /**
         * Searches: The words each wire is searched for. Each search reads the newest 50 releases that hold them. 1
         * to 8 items. Default: ["raises Series A", "seed funding", "raises Series B", "funding round"].
         *
         * @param searchTerms the values
         * @return this builder
         */
        public Builder searchTerms(String... searchTerms) {
            return searchTerms(Arrays.asList(searchTerms));
        }

        /**
         * Newswires: Which newswires to read. Each one of "prnewswire", "globenewswire". At least 1 item. Default:
         * ["prnewswire", "globenewswire"].
         *
         * @param wires the values; not null
         * @return this builder
         */
        public Builder wires(List<String> wires) {
            values.put("wires", List.copyOf(wires));
            return this;
        }

        /**
         * Newswires: Which newswires to read. Each one of "prnewswire", "globenewswire". At least 1 item. Default:
         * ["prnewswire", "globenewswire"].
         *
         * @param wires the values
         * @return this builder
         */
        public Builder wires(String... wires) {
            return wires(Arrays.asList(wires));
        }

        /**
         * Releases read: How many of the newest likely funding releases are read in full. From 5 to 100. Default:
         * 50.
         *
         * @param releasesRead the value
         * @return this builder
         */
        public Builder releasesRead(int releasesRead) {
            values.put("releases_read", releasesRead);
            return this;
        }

        /**
         * Who to contact: The roles of the people to find at each company. Default: ["Founder", "CEO", "Head of
         * Talent", "Head of People"].
         *
         * @param contactTitles the values; not null
         * @return this builder
         */
        public Builder contactTitles(List<String> contactTitles) {
            values.put("contact_titles", List.copyOf(contactTitles));
            return this;
        }

        /**
         * Who to contact: The roles of the people to find at each company. Default: ["Founder", "CEO", "Head of
         * Talent", "Head of People"].
         *
         * @param contactTitles the values
         * @return this builder
         */
        public Builder contactTitles(String... contactTitles) {
            return contactTitles(Arrays.asList(contactTitles));
        }

        /**
         * Contacts per company: 0 turns contacts off. From 0 to 5. Default: 2.
         *
         * @param contactsPerCompany the value
         * @return this builder
         */
        public Builder contactsPerCompany(int contactsPerCompany) {
            values.put("contacts_per_company", contactsPerCompany);
            return this;
        }

        /**
         * Only addresses that pass the check: Keeps addresses whose domain takes mail, and those a verification
         * provider confirmed. Default: true.
         *
         * @param requireVerifiedEmail the value
         * @return this builder
         */
        public Builder requireVerifiedEmail(boolean requireVerifiedEmail) {
            values.put("require_verified_email", requireVerifiedEmail);
            return this;
        }

        /**
         * Show a company again after (days): For a saved formula, how long before a delivered company may come
         * back. From 1 to 365. Default: 90.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many rows, best first. From 1 to 1000. Default: 100.
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
        public FundedAndHiringParams build() {
            return new FundedAndHiringParams(values);
        }
    }
}
