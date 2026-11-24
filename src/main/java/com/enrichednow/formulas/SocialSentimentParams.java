// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Social Sentiment ({@code social-sentiment}): How often the tickers and brands you follow are
 * mentioned on social sources, in what tone, and how both moved since the last run.
 *
 * Only {@code terms} is required: {@link Builder#build()} refuses to build without it. A value not set is not sent,
 * and the formula's default applies.
 *
 * <pre>{@code
 * SocialSentimentParams params = SocialSentimentParams.builder()
 *     .terms("Nvidia", "Tesla")
 *     .build();
 * }</pre>
 */
public final class SocialSentimentParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "social-sentiment";

    private final Map<String, Object> values;

    private SocialSentimentParams(Map<String, Object> values) {
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
        return "SocialSentimentParams" + values;
    }

    /** Builds {@link SocialSentimentParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Tickers or brands: Each is searched as you type it and counted where a post names it as a whole word,
         * such as Nvidia or $NVDA. Mastodon is searched by hashtag, so only one-word terms are read there. 1 to 10
         * items. Required.
         *
         * @param terms the values; not null
         * @return this builder
         */
        public Builder terms(List<String> terms) {
            values.put("terms", List.copyOf(terms));
            return this;
        }

        /**
         * Tickers or brands: Each is searched as you type it and counted where a post names it as a whole word,
         * such as Nvidia or $NVDA. Mastodon is searched by hashtag, so only one-word terms are read there. 1 to 10
         * items. Required.
         *
         * @param terms the values
         * @return this builder
         */
        public Builder terms(String... terms) {
            return terms(Arrays.asList(terms));
        }

        /**
         * Sources: Where to look. Telegram, X and Bluesky are read through the channels and accounts named below.
         * Each one of "hackernews", "mastodon", "youtube", "telegram", "x", "bluesky", "stackoverflow". At least 1
         * item. Default: ["hackernews", "mastodon", "youtube", "telegram", "x", "bluesky"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * Sources: Where to look. Telegram, X and Bluesky are read through the channels and accounts named below.
         * Each one of "hackernews", "mastodon", "youtube", "telegram", "x", "bluesky", "stackoverflow". At least 1
         * item. Default: ["hackernews", "mastodon", "youtube", "telegram", "x", "bluesky"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * Mentions of the last (days): Only mentions posted in the last this many days are counted. YouTube
         * comments carry no date and are not cut. From 1 to 30. Default: 7.
         *
         * @param windowDays the value
         * @return this builder
         */
        public Builder windowDays(int windowDays) {
            values.put("window_days", windowDays);
            return this;
        }

        /**
         * Posts read per term on Mastodon and Telegram: The most posts read for one term on Mastodon and in each
         * Telegram channel, newest first. Hacker News and Stack Overflow are read 100 deep. From 5 to 100. Default:
         * 40.
         *
         * @param perSourceLimit the value
         * @return this builder
         */
        public Builder perSourceLimit(int perSourceLimit) {
            values.put("per_source_limit", perSourceLimit);
            return this;
        }

        /**
         * Telegram channels: Public channels whose posts are searched for each term, by name (WatcherGuru) or
         * address. At most 10 items. Default: ["WatcherGuru", "financialjuice"].
         *
         * @param telegramChannels the values; not null
         * @return this builder
         */
        public Builder telegramChannels(List<String> telegramChannels) {
            values.put("telegram_channels", List.copyOf(telegramChannels));
            return this;
        }

        /**
         * Telegram channels: Public channels whose posts are searched for each term, by name (WatcherGuru) or
         * address. At most 10 items. Default: ["WatcherGuru", "financialjuice"].
         *
         * @param telegramChannels the values
         * @return this builder
         */
        public Builder telegramChannels(String... telegramChannels) {
            return telegramChannels(Arrays.asList(telegramChannels));
        }

        /**
         * X accounts: Accounts whose latest posts (X shows the newest five of each) are read for mentions. At most
         * 20 items. Default: ["DeItaone", "unusual_whales", "zerohedge", "StockMKTNewz", "WatcherGuru"].
         *
         * @param xAccounts the values; not null
         * @return this builder
         */
        public Builder xAccounts(List<String> xAccounts) {
            values.put("x_accounts", List.copyOf(xAccounts));
            return this;
        }

        /**
         * X accounts: Accounts whose latest posts (X shows the newest five of each) are read for mentions. At most
         * 20 items. Default: ["DeItaone", "unusual_whales", "zerohedge", "StockMKTNewz", "WatcherGuru"].
         *
         * @param xAccounts the values
         * @return this builder
         */
        public Builder xAccounts(String... xAccounts) {
            return xAccounts(Arrays.asList(xAccounts));
        }

        /**
         * Bluesky accounts: Accounts by handle, such as reuters.com, whose latest posts are read for mentions. At
         * most 20 items. Default: ["bloomberg.com", "reuters.com", "wsj.com"].
         *
         * @param blueskyAccounts the values; not null
         * @return this builder
         */
        public Builder blueskyAccounts(List<String> blueskyAccounts) {
            values.put("bluesky_accounts", List.copyOf(blueskyAccounts));
            return this;
        }

        /**
         * Bluesky accounts: Accounts by handle, such as reuters.com, whose latest posts are read for mentions. At
         * most 20 items. Default: ["bloomberg.com", "reuters.com", "wsj.com"].
         *
         * @param blueskyAccounts the values
         * @return this builder
         */
        public Builder blueskyAccounts(String... blueskyAccounts) {
            return blueskyAccounts(Arrays.asList(blueskyAccounts));
        }

        /**
         * Bluesky posts read per account: The newest posts read from each Bluesky account. From 5 to 100. Default:
         * 30.
         *
         * @param postsPerAccount the value
         * @return this builder
         */
        public Builder postsPerAccount(int postsPerAccount) {
            values.put("posts_per_account", postsPerAccount);
            return this;
        }

        /**
         * YouTube videos per term: How many of the videos a YouTube search for the term finds first have their
         * comments read. From 1 to 5. Default: 2.
         *
         * @param videosPerTerm the value
         * @return this builder
         */
        public Builder videosPerTerm(int videosPerTerm) {
            values.put("videos_per_term", videosPerTerm);
            return this;
        }

        /**
         * Comments read per video: The newest comments read on each video. From 5 to 100. Default: 20.
         *
         * @param commentsPerVideo the value
         * @return this builder
         */
        public Builder commentsPerVideo(int commentsPerVideo) {
            values.put("comments_per_video", commentsPerVideo);
            return this;
        }

        /**
         * Mastodon server: The server whose view of a hashtag is read; a large one sees the posts of many others.
         * Default: "mastodon.social".
         *
         * @param mastodonServer the value
         * @return this builder
         */
        public Builder mastodonServer(String mastodonServer) {
            values.put("mastodon_server", mastodonServer);
            return this;
        }

        /**
         * Positive words: A mention that uses one of these as a whole word, and none of the negative ones, counts
         * as positive. At most 100 items. Default: a list of 44 values, which {@code get} on the formula shows.
         *
         * @param positiveWords the values; not null
         * @return this builder
         */
        public Builder positiveWords(List<String> positiveWords) {
            values.put("positive_words", List.copyOf(positiveWords));
            return this;
        }

        /**
         * Positive words: A mention that uses one of these as a whole word, and none of the negative ones, counts
         * as positive. At most 100 items. Default: a list of 44 values, which {@code get} on the formula shows.
         *
         * @param positiveWords the values
         * @return this builder
         */
        public Builder positiveWords(String... positiveWords) {
            return positiveWords(Arrays.asList(positiveWords));
        }

        /**
         * Negative words: A mention that uses one of these as a whole word, and none of the positive ones, counts
         * as negative. At most 100 items. Default: a list of 52 values, which {@code get} on the formula shows.
         *
         * @param negativeWords the values; not null
         * @return this builder
         */
        public Builder negativeWords(List<String> negativeWords) {
            values.put("negative_words", List.copyOf(negativeWords));
            return this;
        }

        /**
         * Negative words: A mention that uses one of these as a whole word, and none of the positive ones, counts
         * as negative. At most 100 items. Default: a list of 52 values, which {@code get} on the formula shows.
         *
         * @param negativeWords the values
         * @return this builder
         */
        public Builder negativeWords(String... negativeWords) {
            return negativeWords(Arrays.asList(negativeWords));
        }

        /**
         * Most rows: The run stops at this many rows, the biggest moves first. From 1 to 100. Default: 100.
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
        public SocialSentimentParams build() {
            if (!values.containsKey("terms")) {
                throw new IllegalStateException("SocialSentimentParams: terms is required");
            }
            return new SocialSentimentParams(values);
        }
    }
}
