package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 * One formula with everything needed to run it and read its rows, as {@code GET /v1/formulas/{id}} returns it: the JSON
 * Schema of its parameters, presets, the columns of a row, what it needs and whether this account has it, sample rows
 * and its changelog.
 */
public final class FormulaDetails extends Formula {
    private final String description;
    private final Map<String, Object> parameters;
    private final List<Preset> presets;
    private final List<Step> steps;
    private final List<Column> columns;
    private final CompanyView companyView;
    private final Requirements requirements;
    private final Map<String, Long> limits;
    private final List<Map<String, Object>> sampleRows;
    private final boolean sampleIsFixture;
    private final List<String> changelog;

    @JsonCreator
    FormulaDetails(@JsonProperty("id") String id,
                   @JsonProperty("version") String version,
                   @JsonProperty("title") String title,
                   @JsonProperty("summary") String summary,
                   @JsonProperty("use_when") String useWhen,
                   @JsonProperty("category") String category,
                   @JsonProperty("personas") List<String> personas,
                   @JsonProperty("status") String status,
                   @JsonProperty("available") boolean available,
                   @JsonProperty("needs_provider") boolean needsProvider,
                   @JsonProperty("recommended_cadence") String recommendedCadence,
                   @JsonProperty("schedules") List<String> schedules,
                   @JsonProperty("needs_attestation") boolean needsAttestation,
                   @JsonProperty("health") FormulaHealth health,
                   @JsonProperty("basics") List<FormulaInput> basics,
                   @JsonProperty("description") String description,
                   @JsonProperty("parameters") Map<String, Object> parameters,
                   @JsonProperty("presets") List<Preset> presets,
                   @JsonProperty("steps") List<Step> steps,
                   @JsonProperty("columns") List<Column> columns,
                   @JsonProperty("company_view") CompanyView companyView,
                   @JsonProperty("requirements") Requirements requirements,
                   @JsonProperty("limits") Map<String, Long> limits,
                   @JsonProperty("sample_rows") List<Map<String, Object>> sampleRows,
                   @JsonProperty("sample_is_fixture") boolean sampleIsFixture,
                   @JsonProperty("changelog") List<String> changelog) {
        super(id, version, title, summary, useWhen, category, personas, status, available, needsProvider, recommendedCadence, schedules,
                needsAttestation, health, basics);
        this.description = description;
        this.parameters = Values.map(parameters);
        this.presets = Values.list(presets);
        this.steps = Values.list(steps);
        this.columns = Values.list(columns);
        this.companyView = companyView;
        this.requirements = requirements;
        this.limits = Values.map(limits);
        this.sampleRows = Values.list(sampleRows);
        this.sampleIsFixture = sampleIsFixture;
        this.changelog = Values.list(changelog);
    }

    /** @return what it does, in a paragraph */
    public String description() {
        return description;
    }

    /**
     * @return the JSON Schema of its parameters, as a {@code Map}: their names, types, defaults and bounds; the
     *     {@code required} ones are all a run needs
     */
    public Map<String, Object> parameters() {
        return parameters;
    }

    /** @return ready-made sets of parameters to start from */
    public List<Preset> presets() {
        return presets;
    }

    /** @return the steps a run goes through */
    public List<Step> steps() {
        return steps;
    }

    /** @return the columns of a row, in their order */
    public List<Column> columns() {
        return columns;
    }

    /**
     * @return when set, its rows can also be read one row per company ({@code FormulaRowListParams.View.COMPANY});
     *     otherwise null
     */
    public CompanyView companyView() {
        return companyView;
    }

    /** @return what it reads from and needs, and what of that this account or deployment lacks */
    public Requirements requirements() {
        return requirements;
    }

    /**
     * @return its limits by name: {@code default_rows}, {@code max_rows}, {@code default_unit_cap}, {@code max_unit_cap},
     *     {@code min_unit_cap} and so on
     */
    public Map<String, Long> limits() {
        return limits;
    }

    /** @return what a row looks like: free to read, before any units are spent */
    public List<Map<String, Object>> sampleRows() {
        return sampleRows;
    }

    /** @return true while the sample is made-up data of the right shape, not rows of a real run */
    public boolean sampleIsFixture() {
        return sampleIsFixture;
    }

    /** @return what changed in each version, oldest first */
    public List<String> changelog() {
        return changelog;
    }

    /** A ready-made set of parameters. */
    public static final class Preset {
        private final String id;
        private final String label;
        private final Map<String, Object> params;

        @JsonCreator
        Preset(@JsonProperty("id") String id, @JsonProperty("label") String label, @JsonProperty("params") Map<String, Object> params) {
            this.id = id;
            this.label = label;
            this.params = Values.map(params);
        }

        /** @return the preset's id */
        public String id() {
            return id;
        }

        /** @return its name */
        public String label() {
            return label;
        }

        /** @return the parameters it sets: run with them as they are, or change some first */
        public Map<String, Object> params() {
            return params;
        }

        @Override
        public String toString() {
            return "Preset{id=" + id + ", label=" + label + '}';
        }
    }

    /** One step of a run. */
    public static final class Step {
        private final String id;
        private final String kind;
        private final String label;

        @JsonCreator
        Step(@JsonProperty("id") String id, @JsonProperty("kind") String kind, @JsonProperty("label") String label) {
            this.id = id;
            this.kind = kind;
            this.label = label;
        }

        /** @return the step's id, as a run's {@link FormulaRunStep#id()} names it */
        public String id() {
            return id;
        }

        /** @return what kind of step it is, e.g. {@code scrape}, {@code filter}, {@code enrich} */
        public String kind() {
            return kind;
        }

        /** @return its label for a person, or null */
        public String label() {
            return label;
        }

        @Override
        public String toString() {
            return "Step{id=" + id + ", kind=" + kind + '}';
        }
    }

    /** One column of a row. */
    public static final class Column {
        private final String key;
        private final String label;
        private final String type;
        private final boolean defaultVisible;

        @JsonCreator
        Column(@JsonProperty("key") String key, @JsonProperty("label") String label, @JsonProperty("type") String type,
               @JsonProperty("default_visible") boolean defaultVisible) {
            this.key = key;
            this.label = label;
            this.type = type;
            this.defaultVisible = defaultVisible;
        }

        /** @return the column's key in {@link FormulaRow#data()}, and as {@code fields} names it */
        public String key() {
            return key;
        }

        /** @return its heading for a person */
        public String label() {
            return label;
        }

        /** @return the type of its values, e.g. {@code string}, {@code integer}, {@code list} */
        public String type() {
            return type;
        }

        /** @return whether the console shows it unless asked not to */
        public boolean defaultVisible() {
            return defaultVisible;
        }

        @Override
        public String toString() {
            return "Column{key=" + key + ", type=" + type + '}';
        }
    }

    /** How the rows read one row per company. */
    public static final class CompanyView {
        private final String key;
        private final List<String> lists;
        private final String label;

        @JsonCreator
        CompanyView(@JsonProperty("key") String key, @JsonProperty("lists") List<String> lists, @JsonProperty("label") String label) {
            this.key = key;
            this.lists = Values.list(lists);
            this.label = label;
        }

        /** @return the column that names the company, e.g. {@code company_domain} */
        public String key() {
            return key;
        }

        /** @return the columns gathered into a list of the company's values */
        public List<String> lists() {
            return lists;
        }

        /** @return its name for a person */
        public String label() {
            return label;
        }

        @Override
        public String toString() {
            return "CompanyView{key=" + key + ", lists=" + lists + '}';
        }
    }

    /** What a formula reads from and needs, and what of that this account or deployment lacks. */
    public static final class Requirements {
        private final List<String> scrapers;
        private final List<String> capabilities;
        private final List<String> capabilitiesConnected;
        private final List<String> products;
        private final List<String> missing;
        private final List<String> disabledSources;
        private final List<String> notOffered;

        @JsonCreator
        Requirements(@JsonProperty("scrapers") List<String> scrapers,
                     @JsonProperty("capabilities") List<String> capabilities,
                     @JsonProperty("capabilities_connected") List<String> capabilitiesConnected,
                     @JsonProperty("products") List<String> products,
                     @JsonProperty("missing") List<String> missing,
                     @JsonProperty("disabled_sources") List<String> disabledSources,
                     @JsonProperty("not_offered") List<String> notOffered) {
            this.scrapers = Values.list(scrapers);
            this.capabilities = Values.list(capabilities);
            this.capabilitiesConnected = Values.list(capabilitiesConnected);
            this.products = Values.list(products);
            this.missing = Values.list(missing);
            this.disabledSources = Values.list(disabledSources);
            this.notOffered = Values.list(notOffered);
        }

        /** @return the scrapers it reads from */
        public List<String> scrapers() {
            return scrapers;
        }

        /** @return the outside services it can use: {@code people_search}, {@code contact_find}, {@code email_verify} */
        public List<String> capabilities() {
            return capabilities;
        }

        /** @return of those, what this account has connected */
        public List<String> capabilitiesConnected() {
            return capabilitiesConnected;
        }

        /** @return the products the account needs, e.g. {@code scrapers}, {@code data} */
        public List<String> products() {
            return products;
        }

        /** @return what this account or deployment lacks for it to run at all; empty when nothing is */
        public List<String> missing() {
            return missing;
        }

        /** @return sources switched off for now: runs go on without them, and say so */
        public List<String> disabledSources() {
            return disabledSources;
        }

        /** @return sources this deployment does not offer: a run goes on without them and names them in its reasons */
        public List<String> notOffered() {
            return notOffered;
        }

        @Override
        public String toString() {
            return "Requirements{products=" + products + ", missing=" + missing + ", notOffered=" + notOffered + '}';
        }
    }
}
