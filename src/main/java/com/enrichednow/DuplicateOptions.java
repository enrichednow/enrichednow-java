package com.enrichednow;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * How {@link SavedFormulaService#duplicate} copies a saved formula: the copy has the same formula, parameters, caps
 * and notices; by default it starts with nothing delivered and no schedule.
 */
public final class DuplicateOptions {
    private static final DuplicateOptions NONE = builder().build();

    private final Map<String, Object> body;

    private DuplicateOptions(Map<String, Object> body) {
        this.body = Collections.unmodifiableMap(body);
    }

    /** @return the defaults: named after the original, nothing delivered, no schedule */
    public static DuplicateOptions none() {
        return NONE;
    }

    /** @return a builder */
    public static Builder builder() {
        return new Builder();
    }

    Map<String, Object> body() {
        return body;
    }

    /** Builds {@link DuplicateOptions}. */
    public static final class Builder {
        private final Map<String, Object> body = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * @param name the copy's name, 1 to 200 characters
         * @return this builder
         */
        public Builder name(String name) {
            body.put("name", Objects.requireNonNull(name, "name"));
            return this;
        }

        /**
         * @param copySeen true to start with what the original delivered, so the copy does not deliver it again
         * @return this builder
         */
        public Builder copySeen(boolean copySeen) {
            body.put("copy_seen", copySeen);
            return this;
        }

        /**
         * @param schedule the copy's schedule
         * @return this builder
         */
        public Builder schedule(Schedule schedule) {
            body.put("schedule", Objects.requireNonNull(schedule, "schedule").toMap());
            return this;
        }

        /** @return the options */
        public DuplicateOptions build() {
            return new DuplicateOptions(new LinkedHashMap<>(body));
        }
    }
}
