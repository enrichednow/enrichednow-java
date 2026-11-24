// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Directory to Leads ({@code directory-to-leads}): The businesses a directory lists, each with
 * its website and the email that site publishes, checked.
 *
 * Only {@code directory}, {@code what} are required: {@link Builder#build()} refuses to build without them. A value
 * not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * DirectoryToLeadsParams params = DirectoryToLeadsParams.builder()
 *     .directory("bbb")
 *     .what("plumber")
 *     .build();
 * }</pre>
 */
public final class DirectoryToLeadsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "directory-to-leads";

    private final Map<String, Object> values;

    private DirectoryToLeadsParams(Map<String, Object> values) {
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
        return "DirectoryToLeadsParams" + values;
    }

    /** Builds {@link DirectoryToLeadsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Directory: Which directory to read; a web page is any directory page of your own, such as a chamber of
         * commerce's members. One of "bbb", "gelbeseiten", "checkatrade", "clutch", "web_page". Required.
         *
         * @param directory the value
         * @return this builder
         */
        public Builder directory(String directory) {
            values.put("directory", directory);
            return this;
        }

        /**
         * Kind of business: As the directory names it: plumber on BBB or Checkatrade, Zahnarzt on Gelbe Seiten, a
         * Clutch directory with its place such as web-developers/london, or the address of the directory page.
         * Required.
         *
         * @param what the value
         * @return this builder
         */
        public Builder what(String what) {
            values.put("what", what);
            return this;
        }

        /**
         * Place: A town, city or postcode. Not needed for Clutch, whose place is in its directory, or for a web
         * page. Default: "".
         *
         * @param where the value
         * @return this builder
         */
        public Builder where(String where) {
            values.put("where", where);
            return this;
        }

        /**
         * Pages to read: How many pages of the directory to read; for a web page, how many of its pages to follow.
         * From 1 to 10. Default: 3.
         *
         * @param pages the value
         * @return this builder
         */
        public Builder pages(int pages) {
            values.put("pages", pages);
            return this;
        }

        /**
         * Only businesses with an email: Keeps the businesses whose own website publishes an email address that
         * passes the check. Default: true.
         *
         * @param requireEmail the value
         * @return this builder
         */
        public Builder requireEmail(boolean requireEmail) {
            values.put("require_email", requireEmail);
            return this;
        }

        /**
         * Only named addresses: Leaves out shared inboxes such as info{@literal @}. Default: false.
         *
         * @param excludeRoleAddresses the value
         * @return this builder
         */
        public Builder excludeRoleAddresses(boolean excludeRoleAddresses) {
            values.put("exclude_role_addresses", excludeRoleAddresses);
            return this;
        }

        /**
         * Fewest reviews: Businesses with fewer reviews on the directory are left out. At least 0. Default: 0.
         *
         * @param minReviews the value
         * @return this builder
         */
        public Builder minReviews(int minReviews) {
            values.put("min_reviews", minReviews);
            return this;
        }

        /**
         * Businesses to leave out: Names or domains, such as your existing customers. At most 5000 items. Default:
         * [].
         *
         * @param excludeCompanies the values; not null
         * @return this builder
         */
        public Builder excludeCompanies(List<String> excludeCompanies) {
            values.put("exclude_companies", List.copyOf(excludeCompanies));
            return this;
        }

        /**
         * Businesses to leave out: Names or domains, such as your existing customers. At most 5000 items. Default:
         * [].
         *
         * @param excludeCompanies the values
         * @return this builder
         */
        public Builder excludeCompanies(String... excludeCompanies) {
            return excludeCompanies(Arrays.asList(excludeCompanies));
        }

        /**
         * Show a business again after (days): For a saved formula, how long before a delivered business may come
         * back. From 1 to 365. Default: 180.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many businesses. From 1 to 5000. Default: 200.
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
        public DirectoryToLeadsParams build() {
            if (!values.containsKey("directory")) {
                throw new IllegalStateException("DirectoryToLeadsParams: directory is required");
            }
            if (!values.containsKey("what")) {
                throw new IllegalStateException("DirectoryToLeadsParams: what is required");
            }
            return new DirectoryToLeadsParams(values);
        }
    }
}
