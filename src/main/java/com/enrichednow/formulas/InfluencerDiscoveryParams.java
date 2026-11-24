// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Influencer Discovery ({@code influencer-discovery}): Creators who make videos about your topic,
 * with followers, views, engagement, their links and a published business email.
 *
 * Only {@code topics} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * InfluencerDiscoveryParams params = InfluencerDiscoveryParams.builder()
 *     .topics("home espresso", "latte art")
 *     .build();
 * }</pre>
 */
public final class InfluencerDiscoveryParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "influencer-discovery";

    private final Map<String, Object> values;

    private InfluencerDiscoveryParams(Map<String, Object> values) {
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
        return "InfluencerDiscoveryParams" + values;
    }

    /** Builds {@link InfluencerDiscoveryParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Topics: What the creators make videos about, as a viewer would search it, such as home espresso. A
         * hashtag works too. 1 to 10 items. Required.
         *
         * @param topics the values; not null
         * @return this builder
         */
        public Builder topics(List<String> topics) {
            values.put("topics", List.copyOf(topics));
            return this;
        }

        /**
         * Topics: What the creators make videos about, as a viewer would search it, such as home espresso. A
         * hashtag works too. 1 to 10 items. Required.
         *
         * @param topics the values
         * @return this builder
         */
        public Builder topics(String... topics) {
            return topics(Arrays.asList(topics));
        }

        /**
         * Subscribers: The size of creator to keep, by YouTube subscribers. Default: {"min": 10000, "max":
         * 1000000}.
         *
         * @param min the lowest, included
         * @param max the highest, included
         * @return this builder
         */
        public Builder subscribers(int min, int max) {
            Map<String, Object> range = new LinkedHashMap<>();
            range.put("min", min);
            range.put("max", max);
            values.put("subscribers", Collections.unmodifiableMap(range));
            return this;
        }

        /**
         * Countries: Only creators whose channel states one of these countries, as YouTube writes it (United
         * States, Germany). Leave empty for any; a channel that states none is then kept. At most 20 items.
         * Default: [].
         *
         * @param countries the values; not null
         * @return this builder
         */
        public Builder countries(List<String> countries) {
            values.put("countries", List.copyOf(countries));
            return this;
        }

        /**
         * Countries: Only creators whose channel states one of these countries, as YouTube writes it (United
         * States, Germany). Leave empty for any; a channel that states none is then kept. At most 20 items.
         * Default: [].
         *
         * @param countries the values
         * @return this builder
         */
        public Builder countries(String... countries) {
            return countries(Arrays.asList(countries));
        }

        /**
         * Least videos on the topic: How many of a creator's videos must turn up in the topic searches; 2 or more
         * keeps creators who cover it regularly. From 1 to 10. Default: 1.
         *
         * @param minTopicVideos the value
         * @return this builder
         */
        public Builder minTopicVideos(int minTopicVideos) {
            values.put("min_topic_videos", minTopicVideos);
            return this;
        }

        /**
         * Creators to leave out: Handles ({@literal @}name) or channel ids of creators you already work with. At
         * most 1000 items. Default: [].
         *
         * @param excludeChannels the values; not null
         * @return this builder
         */
        public Builder excludeChannels(List<String> excludeChannels) {
            values.put("exclude_channels", List.copyOf(excludeChannels));
            return this;
        }

        /**
         * Creators to leave out: Handles ({@literal @}name) or channel ids of creators you already work with. At
         * most 1000 items. Default: [].
         *
         * @param excludeChannels the values
         * @return this builder
         */
        public Builder excludeChannels(String... excludeChannels) {
            return excludeChannels(Arrays.asList(excludeChannels));
        }

        /**
         * Videos read per topic: How many of each topic's videos are read to find creators. Each video read is one
         * unit. From 10 to 100. Default: 40.
         *
         * @param videosPerTopic the value
         * @return this builder
         */
        public Builder videosPerTopic(int videosPerTopic) {
            values.put("videos_per_topic", videosPerTopic);
            return this;
        }

        /**
         * Creators read: How many creators' channels are read, the most on topic first; each channel, profile and
         * website read is one unit. From 5 to 200. Default: 40.
         *
         * @param maxCreators the value
         * @return this builder
         */
        public Builder maxCreators(int maxCreators) {
            values.put("max_creators", maxCreators);
            return this;
        }

        /**
         * Also read: The creator's Instagram and TikTok accounts, where their channel links to them. Each one of
         * "instagram", "tiktok". Default: ["instagram", "tiktok"].
         *
         * @param otherPlatforms the values; not null
         * @return this builder
         */
        public Builder otherPlatforms(List<String> otherPlatforms) {
            values.put("other_platforms", List.copyOf(otherPlatforms));
            return this;
        }

        /**
         * Also read: The creator's Instagram and TikTok accounts, where their channel links to them. Each one of
         * "instagram", "tiktok". Default: ["instagram", "tiktok"].
         *
         * @param otherPlatforms the values
         * @return this builder
         */
        public Builder otherPlatforms(String... otherPlatforms) {
            return otherPlatforms(Arrays.asList(otherPlatforms));
        }

        /**
         * Business email from their website: Reads the creator's own website, where their channel links to one, for
         * the business address it publishes. Default: true.
         *
         * @param websiteEmail the value
         * @return this builder
         */
        public Builder websiteEmail(boolean websiteEmail) {
            values.put("website_email", websiteEmail);
            return this;
        }

        /**
         * Most rows: The run stops at this many creators, best fit first. From 1 to 1000. Default: 100.
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
        public InfluencerDiscoveryParams build() {
            if (!values.containsKey("topics")) {
                throw new IllegalStateException("InfluencerDiscoveryParams: topics is required");
            }
            return new InfluencerDiscoveryParams(values);
        }
    }
}
