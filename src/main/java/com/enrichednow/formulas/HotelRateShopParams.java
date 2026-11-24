// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of Hotel Rate Shop ({@code hotel-rate-shop}): What your competing hotels charge a night for the
 * same stays, from Google Hotels, Booking.com and MakeMyTrip, with each change.
 *
 * Only {@code hotels} is required: {@link Builder#build()} refuses to build without it. A value not set is not
 * sent, and the formula's default applies.
 *
 * <pre>{@code
 * HotelRateShopParams params = HotelRateShopParams.builder()
 *     .hotels("Strand Palace", "Hotel AMANO Covent Garden")
 *     .build();
 * }</pre>
 */
public final class HotelRateShopParams implements FormulaParams {
    /** The formula these parameters are for. */
    public static final String FORMULA_ID = "hotel-rate-shop";

    private final Map<String, Object> values;

    private HotelRateShopParams(Map<String, Object> values) {
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
        return "HotelRateShopParams" + values;
    }

    /** Builds {@link HotelRateShopParams}. */
    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Hotels: Each hotel's name as Google Hotels shows it. Its rate there is read when the first hotel Google
         * Hotels finds has that name. 1 to 20 items. Required.
         *
         * @param hotels the values; not null
         * @return this builder
         */
        public Builder hotels(List<String> hotels) {
            values.put("hotels", List.copyOf(hotels));
            return this;
        }

        /**
         * Hotels: Each hotel's name as Google Hotels shows it. Its rate there is read when the first hotel Google
         * Hotels finds has that name. 1 to 20 items. Required.
         *
         * @param hotels the values
         * @return this builder
         */
        public Builder hotels(String... hotels) {
            return hotels(Arrays.asList(hotels));
        }

        /**
         * Town: The town the hotels are in, added to each name when it is searched, such as London. Leave empty
         * when the names say it. Default: "".
         *
         * @param city the value
         * @return this builder
         */
        public Builder city(String city) {
            values.put("city", city);
            return this;
        }

        /**
         * Stays starting in (days): How many days after each run the stays begin. A saved formula asks the same
         * distances every time it runs. 1 to 6 items. Default: [7, 14, 30].
         *
         * @param daysAhead the values; not null
         * @return this builder
         */
        public Builder daysAhead(List<Integer> daysAhead) {
            values.put("days_ahead", List.copyOf(daysAhead));
            return this;
        }

        /**
         * Stays starting in (days): How many days after each run the stays begin. A saved formula asks the same
         * distances every time it runs. 1 to 6 items. Default: [7, 14, 30].
         *
         * @param daysAhead the values
         * @return this builder
         */
        public Builder daysAhead(Integer... daysAhead) {
            return daysAhead(Arrays.asList(daysAhead));
        }

        /**
         * Booking.com pages: The hotels' pages on Booking.com, such as
         * https://www.booking.com/hotel/gb/strandpalace.html, to read their room prices there too. At most 20
         * items. Default: [].
         *
         * @param bookingPages the values; not null
         * @return this builder
         */
        public Builder bookingPages(List<String> bookingPages) {
            values.put("booking_pages", List.copyOf(bookingPages));
            return this;
        }

        /**
         * Booking.com pages: The hotels' pages on Booking.com, such as
         * https://www.booking.com/hotel/gb/strandpalace.html, to read their room prices there too. At most 20
         * items. Default: [].
         *
         * @param bookingPages the values
         * @return this builder
         */
        public Builder bookingPages(String... bookingPages) {
            return bookingPages(Arrays.asList(bookingPages));
        }

        /**
         * MakeMyTrip hotels: The hotels' pages on MakeMyTrip, or their hotel ids there, to read their rates in
         * rupees too. At most 20 items. Default: [].
         *
         * @param makemytripHotels the values; not null
         * @return this builder
         */
        public Builder makemytripHotels(List<String> makemytripHotels) {
            values.put("makemytrip_hotels", List.copyOf(makemytripHotels));
            return this;
        }

        /**
         * MakeMyTrip hotels: The hotels' pages on MakeMyTrip, or their hotel ids there, to read their rates in
         * rupees too. At most 20 items. Default: [].
         *
         * @param makemytripHotels the values
         * @return this builder
         */
        public Builder makemytripHotels(String... makemytripHotels) {
            return makemytripHotels(Arrays.asList(makemytripHotels));
        }

        /**
         * Nights: How long each stay is. Prices are shown a night. From 1 to 14. Default: 1.
         *
         * @param nights the value
         * @return this builder
         */
        public Builder nights(int nights) {
            values.put("nights", nights);
            return this;
        }

        /**
         * Guests: Adults in one room. On Booking.com only the offers for at least this many guests count. From 1 to
         * 8. Default: 2.
         *
         * @param adults the value
         * @return this builder
         */
        public Builder adults(int adults) {
            values.put("adults", adults);
            return this;
        }

        /**
         * Currency: The currency of the Google Hotels and Booking.com prices. MakeMyTrip prices are in rupees. One
         * of "USD", "EUR", "GBP", "AED", "INR", "AUD", "CAD", "JPY", "CHF", "SGD". Default: "USD".
         *
         * @param currency the value
         * @return this builder
         */
        public Builder currency(String currency) {
            values.put("currency", currency);
            return this;
        }

        /**
         * Most rows: The run stops at this many rates, changes and sold-out hotels first. From 1 to 2000. Default:
         * 500.
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
        public HotelRateShopParams build() {
            if (!values.containsKey("hotels")) {
                throw new IllegalStateException("HotelRateShopParams: hotels is required");
            }
            return new HotelRateShopParams(values);
        }
    }
}
