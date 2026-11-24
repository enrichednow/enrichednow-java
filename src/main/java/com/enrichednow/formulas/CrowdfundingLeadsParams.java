// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Crowdfunding Leads ({@code crowdfunding-leads}): Kickstarter and Indiegogo projects with
 * traction, each with funding, backers, the creator's website and the email it publishes.
 *
 * Only {@code keywords} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * CrowdfundingLeadsParams params = CrowdfundingLeadsParams.builder()
 *     .keywords("3d printer", "smart home")
 *     .build();
 * }</pre>
 */
public final class CrowdfundingLeadsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "crowdfunding-leads";

    private final Map<String, Object> values;

    private CrowdfundingLeadsParams(Map<String, Object> values) {
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
        return "CrowdfundingLeadsParams" + values;
    }

    /** Builds {@link CrowdfundingLeadsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Keywords: What the projects make or are about, each searched on each platform, such as e-bike or tabletop
         * game. 1 to 10 items. Required.
         *
         * @param keywords the values; not null
         * @return this builder
         */
        public Builder keywords(List<String> keywords) {
            values.put("keywords", List.copyOf(keywords));
            return this;
        }

        /**
         * Keywords: What the projects make or are about, each searched on each platform, such as e-bike or tabletop
         * game. 1 to 10 items. Required.
         *
         * @param keywords the values
         * @return this builder
         */
        public Builder keywords(String... keywords) {
            return keywords(Arrays.asList(keywords));
        }

        /**
         * Campaign stage: Live campaigns, campaigns that ended funded (the ones that now have to make and ship), or
         * both. One of "live", "funded", "any". Default: "live".
         *
         * @param stage the value
         * @return this builder
         */
        public Builder stage(String stage) {
            values.put("stage", stage);
            return this;
        }

        /**
         * Platforms: Where to search. Each one of "kickstarter", "indiegogo". At least 1 item. Default:
         * ["kickstarter", "indiegogo"].
         *
         * @param platforms the values; not null
         * @return this builder
         */
        public Builder platforms(List<String> platforms) {
            values.put("platforms", List.copyOf(platforms));
            return this;
        }

        /**
         * Platforms: Where to search. Each one of "kickstarter", "indiegogo". At least 1 item. Default:
         * ["kickstarter", "indiegogo"].
         *
         * @param platforms the values
         * @return this builder
         */
        public Builder platforms(String... platforms) {
            return platforms(Arrays.asList(platforms));
        }

        /**
         * Fewest backers: Projects with fewer backers are left out. At least 0. Default: 10.
         *
         * @param minBackers the value
         * @return this builder
         */
        public Builder minBackers(int minBackers) {
            values.put("min_backers", minBackers);
            return this;
        }

        /**
         * Least funded (% of goal): 100 keeps the projects that reached their goal. A project that states no goal
         * is kept. From 0 to 100000. Default: 0.
         *
         * @param minPercentFunded the value
         * @return this builder
         */
        public Builder minPercentFunded(int minPercentFunded) {
            values.put("min_percent_funded", minPercentFunded);
            return this;
        }

        /**
         * Ended within (days): For funded projects, how recently the campaign ended. Live campaigns always pass.
         * From 1 to 3650. Default: 120.
         *
         * @param endedWithinDays the value
         * @return this builder
         */
        public Builder endedWithinDays(int endedWithinDays) {
            values.put("ended_within_days", endedWithinDays);
            return this;
        }

        /**
         * Only these categories: The platform's categories or tags, or part of them, such as 3D Printing, Product
         * Design or Home. Leave empty for any. At most 30 items. Default: [].
         *
         * @param categories the values; not null
         * @return this builder
         */
        public Builder categories(List<String> categories) {
            values.put("categories", List.copyOf(categories));
            return this;
        }

        /**
         * Only these categories: The platform's categories or tags, or part of them, such as 3D Printing, Product
         * Design or Home. Leave empty for any. At most 30 items. Default: [].
         *
         * @param categories the values
         * @return this builder
         */
        public Builder categories(String... categories) {
            return categories(Arrays.asList(categories));
        }

        /**
         * Only these countries (Kickstarter): Two-letter codes such as US or GB. Kickstarter states a project's
         * country; Indiegogo does not, so its projects are kept. At most 50 items. Default: [].
         *
         * @param countries the values; not null
         * @return this builder
         */
        public Builder countries(List<String> countries) {
            values.put("countries", List.copyOf(countries));
            return this;
        }

        /**
         * Only these countries (Kickstarter): Two-letter codes such as US or GB. Kickstarter states a project's
         * country; Indiegogo does not, so its projects are kept. At most 50 items. Default: [].
         *
         * @param countries the values
         * @return this builder
         */
        public Builder countries(String... countries) {
            return countries(Arrays.asList(countries));
        }

        /**
         * Pages read per keyword: Each page holds about 12 Kickstarter or 24 Indiegogo projects. From 1 to 5.
         * Default: 2.
         *
         * @param pages the value
         * @return this builder
         */
        public Builder pages(int pages) {
            values.put("pages", pages);
            return this;
        }

        /**
         * Only creators with an email: Keeps the projects whose creator's own website publishes an email address
         * that passes the check. Default: true.
         *
         * @param requireEmail the value
         * @return this builder
         */
        public Builder requireEmail(boolean requireEmail) {
            values.put("require_email", requireEmail);
            return this;
        }

        /**
         * Most projects: How many projects, best backed first, are matched to a website; with Only creators with an
         * email, fewer rows may remain. From 1 to 500. Default: 100.
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
        public CrowdfundingLeadsParams build() {
            if (!values.containsKey("keywords")) {
                throw new IllegalStateException("CrowdfundingLeadsParams: keywords is required");
            }
            return new CrowdfundingLeadsParams(values);
        }
    }
}
