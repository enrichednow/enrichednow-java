// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Literature Sweep ({@code literature-sweep}): New papers on your subject from PubMed, arXiv and
 * Crossref, each paper once, with its abstract, DOI and free full text.
 *
 * Only {@code query} is required: {@link Builder#build()} refuses to build without it. A value not set is not sent,
 * and the formula's default applies.
 *
 * <pre>{@code
 * LiteratureSweepParams params = LiteratureSweepParams.builder()
 *     .query("protein language model")
 *     .build();
 * }</pre>
 */
public final class LiteratureSweepParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "literature-sweep";

    private final Map<String, Object> values;

    private LiteratureSweepParams(Map<String, Object> values) {
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
        return "LiteratureSweepParams" + values;
    }

    /** Builds {@link LiteratureSweepParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Subject: Words that describe the subject; every word must appear in a paper's listing. PubMed also reads
         * its own field tags, such as review[pt], when the words are not searched as one phrase. Required.
         *
         * @param query the value
         * @return this builder
         */
        public Builder query(String query) {
            values.put("query", query);
            return this;
        }

        /**
         * Search the words as one phrase: Finds protein language model only where those words stand together, as a
         * term of the field. Crossref reads the words alone either way. Default: false.
         *
         * @param exactPhrase the value
         * @return this builder
         */
        public Builder exactPhrase(boolean exactPhrase) {
            values.put("exact_phrase", exactPhrase);
            return this;
        }

        /**
         * Published within (days): Only papers that appeared in the last this many days; 0 for any date. PubMed
         * counts the day a paper was added to it. From 0 to 3650. Default: 30.
         *
         * @param publishedWithinDays the value
         * @return this builder
         */
        public Builder publishedWithinDays(int publishedWithinDays) {
            values.put("published_within_days", publishedWithinDays);
            return this;
        }

        /**
         * Where to search: PubMed for medicine and life sciences, arXiv for preprints in physics, maths and
         * computing, Crossref for journals of every field. Each one of "pubmed", "arxiv", "crossref", "openalex",
         * "semanticscholar". At least 1 item. Default: ["pubmed", "arxiv", "crossref"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * Where to search: PubMed for medicine and life sciences, arXiv for preprints in physics, maths and
         * computing, Crossref for journals of every field. Each one of "pubmed", "arxiv", "crossref", "openalex",
         * "semanticscholar". At least 1 item. Default: ["pubmed", "arxiv", "crossref"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * Only papers by these authors: A surname, or a name as papers print it (Doudna, Liu DR). Leave empty for
         * any author. At most 50 items. Default: [].
         *
         * @param authors the values; not null
         * @return this builder
         */
        public Builder authors(List<String> authors) {
            values.put("authors", List.copyOf(authors));
            return this;
        }

        /**
         * Only papers by these authors: A surname, or a name as papers print it (Doudna, Liu DR). Leave empty for
         * any author. At most 50 items. Default: [].
         *
         * @param authors the values
         * @return this builder
         */
        public Builder authors(String... authors) {
            return authors(Arrays.asList(authors));
        }

        /**
         * Only these journals: Part of a journal's or server's name; Nature also keeps Nature Methods. Leave empty
         * for any. At most 50 items. Default: [].
         *
         * @param journals the values; not null
         * @return this builder
         */
        public Builder journals(List<String> journals) {
            values.put("journals", List.copyOf(journals));
            return this;
        }

        /**
         * Only these journals: Part of a journal's or server's name; Nature also keeps Nature Methods. Leave empty
         * for any. At most 50 items. Default: [].
         *
         * @param journals the values
         * @return this builder
         */
        public Builder journals(String... journals) {
            return journals(Arrays.asList(journals));
        }

        /**
         * Include preprints: Papers not yet peer reviewed, from arXiv, bioRxiv and medRxiv. Default: true.
         *
         * @param includePreprints the value
         * @return this builder
         */
        public Builder includePreprints(boolean includePreprints) {
            values.put("include_preprints", includePreprints);
            return this;
        }

        /**
         * Only papers free to read: Keeps the papers with a link to free full text. Default: false.
         *
         * @param openAccessOnly the value
         * @return this builder
         */
        public Builder openAccessOnly(boolean openAccessOnly) {
            values.put("open_access_only", openAccessOnly);
            return this;
        }

        /**
         * Pages read per source: Each page is the 100 newest (Crossref, the 100 best matching) papers of one
         * source; raise it for a busy subject or a long window. From 1 to 3. Default: 1.
         *
         * @param pages the value
         * @return this builder
         */
        public Builder pages(int pages) {
            values.put("pages", pages);
            return this;
        }

        /**
         * Read PubMed abstracts: PubMed lists papers without their abstracts; this reads each PubMed paper's own
         * page for its abstract and the day it went online. Default: true.
         *
         * @param readAbstracts the value
         * @return this builder
         */
        public Builder readAbstracts(boolean readAbstracts) {
            values.put("read_abstracts", readAbstracts);
            return this;
        }

        /**
         * Most rows: The run stops at this many papers, best matching first. From 1 to 500. Default: 100.
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
        public LiteratureSweepParams build() {
            if (!values.containsKey("query")) {
                throw new IllegalStateException("LiteratureSweepParams: query is required");
            }
            return new LiteratureSweepParams(values);
        }
    }
}
