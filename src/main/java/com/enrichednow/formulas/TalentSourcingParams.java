// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Talent Sourcing ({@code talent-sourcing}): People who posted that they want to be hired, or
 * show their work, in your role and place, scored on the skills you need.
 *
 * Only {@code job_title}, {@code location} are required: {@link Builder#build()} refuses to build without them. A
 * value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * TalentSourcingParams params = TalentSourcingParams.builder()
 *     .jobTitle("Data Engineer")
 *     .location("London")
 *     .build();
 * }</pre>
 */
public final class TalentSourcingParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "talent-sourcing";

    private final Map<String, Object> values;

    private TalentSourcingParams(Map<String, Object> values) {
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
        return "TalentSourcingParams" + values;
    }

    /** Builds {@link TalentSourcingParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Job title: The role you are hiring for, as people would write it about themselves. Required.
         *
         * @param jobTitle the value
         * @return this builder
         */
        public Builder jobTitle(String jobTitle) {
            values.put("job_title", jobTitle);
            return this;
        }

        /**
         * Location: The city, region or country the people should be in. Write Remote for anywhere. Required.
         *
         * @param location the value
         * @return this builder
         */
        public Builder location(String location) {
            values.put("location", location);
            return this;
        }

        /**
         * Skills: What the role asks for. They score each person, and pick the GitHub projects to look through. At
         * most 10 items. Default: [].
         *
         * @param skills the values; not null
         * @return this builder
         */
        public Builder skills(List<String> skills) {
            values.put("skills", List.copyOf(skills));
            return this;
        }

        /**
         * Skills: What the role asks for. They score each person, and pick the GitHub projects to look through. At
         * most 10 items. Default: [].
         *
         * @param skills the values
         * @return this builder
         */
        public Builder skills(String... skills) {
            return skills(Arrays.asList(skills));
        }

        /**
         * Where to look: GitHub finds only developers whose profile gives the place, and is searched only when
         * skills are given. Each one of "hackernews", "github". At least 1 item. Default: ["hackernews"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * Where to look: GitHub finds only developers whose profile gives the place, and is searched only when
         * skills are given. Each one of "hackernews", "github". At least 1 item. Default: ["hackernews"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * Read more posts: Reads the newest 300 posts that name the place instead of 200, reaching further back.
         * Default: false.
         *
         * @param moreResults the value
         * @return this builder
         */
        public Builder moreResults(boolean moreResults) {
            values.put("more_results", moreResults);
            return this;
        }

        /**
         * Posted within (days): Only posts from the last this many days; an older post may no longer be true. From
         * 7 to 730. Default: 365.
         *
         * @param postedWithinDays the value
         * @return this builder
         */
        public Builder postedWithinDays(int postedWithinDays) {
            values.put("posted_within_days", postedWithinDays);
            return this;
        }

        /**
         * GitHub profiles to read: How many developers' profiles to open, from the projects that use the skills.
         * From 1 to 50. Default: 15.
         *
         * @param githubProfiles the value
         * @return this builder
         */
        public Builder githubProfiles(int githubProfiles) {
            values.put("github_profiles", githubProfiles);
            return this;
        }

        /**
         * Least skills named: Leave out a person who names fewer of the skills than this. From 0 to 10. Default: 0.
         *
         * @param minSkills the value
         * @return this builder
         */
        public Builder minSkills(int minSkills) {
            values.put("min_skills", minSkills);
            return this;
        }

        /**
         * Show a person again after (days): For a saved formula, how long before a delivered person may come back.
         * From 1 to 365. Default: 90.
         *
         * @param resurfaceAfterDays the value
         * @return this builder
         */
        public Builder resurfaceAfterDays(int resurfaceAfterDays) {
            values.put("resurface_after_days", resurfaceAfterDays);
            return this;
        }

        /**
         * Most rows: The run stops at this many people, best first. From 1 to 500. Default: 50.
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
        public TalentSourcingParams build() {
            if (!values.containsKey("job_title")) {
                throw new IllegalStateException("TalentSourcingParams: job_title is required");
            }
            if (!values.containsKey("location")) {
                throw new IllegalStateException("TalentSourcingParams: location is required");
            }
            return new TalentSourcingParams(values);
        }
    }
}
