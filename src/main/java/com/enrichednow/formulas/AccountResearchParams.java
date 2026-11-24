// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Account Research ({@code account-research}): For each company domain, what its site and company
 * page say, a line of research and a compliment built only from facts.
 *
 * Only {@code domains} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * AccountResearchParams params = AccountResearchParams.builder()
 *     .domains("gusto.com", "deputy.com")
 *     .build();
 * }</pre>
 */
public final class AccountResearchParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "account-research";

    private final Map<String, Object> values;

    private AccountResearchParams(Map<String, Object> values) {
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
        return "AccountResearchParams" + values;
    }

    /** Builds {@link AccountResearchParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Company websites: Each company's domain or website address, typed or from a CSV. 1 to 500 items.
         * Required.
         *
         * @param domains the values; not null
         * @return this builder
         */
        public Builder domains(List<String> domains) {
            values.put("domains", List.copyOf(domains));
            return this;
        }

        /**
         * Company websites: Each company's domain or website address, typed or from a CSV. 1 to 500 items.
         * Required.
         *
         * @param domains the values
         * @return this builder
         */
        public Builder domains(String... domains) {
            return domains(Arrays.asList(domains));
        }

        /**
         * Read their LinkedIn company page: When the site links to one; adds industry, size, headquarters and when
         * it was founded. Default: true.
         *
         * @param readCompanyPage the value
         * @return this builder
         */
        public Builder readCompanyPage(boolean readCompanyPage) {
            values.put("read_company_page", readCompanyPage);
            return this;
        }

        /**
         * Read the technologies of their site: The site's platform, analytics and tools, as its own pages reveal
         * them. Default: true.
         *
         * @param readTechnologies the value
         * @return this builder
         */
        public Builder readTechnologies(boolean readTechnologies) {
            values.put("read_technologies", readTechnologies);
            return this;
        }

        /**
         * Most rows: The run stops at this many companies. From 1 to 500. Default: 500.
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
        public AccountResearchParams build() {
            if (!values.containsKey("domains")) {
                throw new IllegalStateException("AccountResearchParams: domains is required");
            }
            return new AccountResearchParams(values);
        }
    }
}
