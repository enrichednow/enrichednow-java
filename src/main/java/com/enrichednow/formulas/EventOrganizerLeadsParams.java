// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Event Organizer Leads ({@code event-organizer-leads}): The groups and organisers running events
 * on your topics in a city, from Meetup and Eventbrite, busiest first.
 *
 * Only {@code topics}, {@code city}, {@code region} are required: {@link Builder#build()} refuses to build without
 * them. A value not set is not sent, and the formula's default applies.
 *
 * <pre>{@code
 * EventOrganizerLeadsParams params = EventOrganizerLeadsParams.builder()
 *     .topics("startup")
 *     .city("Austin")
 *     .region("TX")
 *     .build();
 * }</pre>
 */
public final class EventOrganizerLeadsParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "event-organizer-leads";

    private final Map<String, Object> values;

    private EventOrganizerLeadsParams(Map<String, Object> values) {
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
        return "EventOrganizerLeadsParams" + values;
    }

    /** Builds {@link EventOrganizerLeadsParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Topics: What the events are about, as you would search for them, such as startup, python or yoga. 1 to 8
         * items. Required.
         *
         * @param topics the values; not null
         * @return this builder
         */
        public Builder topics(List<String> topics) {
            values.put("topics", List.copyOf(topics));
            return this;
        }

        /**
         * Topics: What the events are about, as you would search for them, such as startup, python or yoga. 1 to 8
         * items. Required.
         *
         * @param topics the values
         * @return this builder
         */
        public Builder topics(String... topics) {
            return topics(Arrays.asList(topics));
        }

        /**
         * City: One city, by its name alone. Required.
         *
         * @param city the value
         * @return this builder
         */
        public Builder city(String city) {
            values.put("city", city);
            return this;
        }

        /**
         * State or province: For a city in the United States or Canada, its state or province in two letters, which
         * both sites need there; leave it empty elsewhere. Required.
         *
         * @param region the value
         * @return this builder
         */
        public Builder region(String region) {
            values.put("region", region);
            return this;
        }

        /**
         * Country: The country, in two letters, such as US, GB or DE. Default: "US".
         *
         * @param country the value
         * @return this builder
         */
        public Builder country(String country) {
            values.put("country", country);
            return this;
        }

        /**
         * Where to look: Meetup's groups and Eventbrite's organisers. Each one of "meetup", "eventbrite". At least
         * 1 item. Default: ["meetup", "eventbrite"].
         *
         * @param sources the values; not null
         * @return this builder
         */
        public Builder sources(List<String> sources) {
            values.put("sources", List.copyOf(sources));
            return this;
        }

        /**
         * Where to look: Meetup's groups and Eventbrite's organisers. Each one of "meetup", "eventbrite". At least
         * 1 item. Default: ["meetup", "eventbrite"].
         *
         * @param sources the values
         * @return this builder
         */
        public Builder sources(String... sources) {
            return sources(Arrays.asList(sources));
        }

        /**
         * Eventbrite's name for the place: Outside the United States and Canada, the place as Eventbrite's
         * addresses write it, such as united-kingdom--london. Empty there leaves Eventbrite out. Default: "".
         *
         * @param eventbritePlace the value
         * @return this builder
         */
        public Builder eventbritePlace(String eventbritePlace) {
            values.put("eventbrite_place", eventbritePlace);
            return this;
        }

        /**
         * Events within (days): Only events starting in the next this many days count. From 1 to 365. Default: 90.
         *
         * @param withinDays the value
         * @return this builder
         */
        public Builder withinDays(int withinDays) {
            values.put("within_days", withinDays);
            return this;
        }

        /**
         * Fewest group members: Meetup groups with fewer members are left out. Eventbrite does not count followers,
         * so its organisers are kept. At least 0. Default: 0.
         *
         * @param minMembers the value
         * @return this builder
         */
        public Builder minMembers(int minMembers) {
            values.put("min_members", minMembers);
            return this;
        }

        /**
         * Fewest events found: Organisers with fewer of the events found are left out. At least 1. Default: 1.
         *
         * @param minEvents the value
         * @return this builder
         */
        public Builder minEvents(int minEvents) {
            values.put("min_events", minEvents);
            return this;
        }

        /**
         * Show an organiser again after (days): For a saved formula, how long before a delivered organiser may come
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
         * Most rows: The run stops at this many organisers, best first. From 1 to 1000. Default: 200.
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
        public EventOrganizerLeadsParams build() {
            if (!values.containsKey("topics")) {
                throw new IllegalStateException("EventOrganizerLeadsParams: topics is required");
            }
            if (!values.containsKey("city")) {
                throw new IllegalStateException("EventOrganizerLeadsParams: city is required");
            }
            if (!values.containsKey("region")) {
                throw new IllegalStateException("EventOrganizerLeadsParams: region is required");
            }
            return new EventOrganizerLeadsParams(values);
        }
    }
}
