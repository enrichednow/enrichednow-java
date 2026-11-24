package com.enrichednow;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * When a saved formula runs by itself: a cadence, a time of day and a time zone.
 *
 * <pre>{@code
 * Schedule.daily("07:30", "Europe/London");
 * Schedule.weekdays("08:00", "America/New_York");
 * Schedule.weekly(DayOfWeek.MONDAY, "06:00", "UTC");
 * Schedule.monthly(1, "09:00", "Europe/Berlin");
 * }</pre>
 *
 * <p>Which cadences a formula allows is its {@code schedules()}; the shortest the plan allows is
 * {@code client.savedFormulas().limits().minIntervalMinutes()}.
 */
public final class Schedule {
    private final String cadence;
    private final String localTime;
    private final String timezone;
    private final Integer dayOfWeek;
    private final Integer dayOfMonth;

    private Schedule(String cadence, String localTime, String timezone, Integer dayOfWeek, Integer dayOfMonth) {
        this.cadence = cadence;
        this.localTime = checkTime(localTime);
        this.timezone = checkZone(timezone);
        this.dayOfWeek = dayOfWeek;
        this.dayOfMonth = dayOfMonth;
    }

    /**
     * Every hour, where the formula and the plan allow it.
     *
     * @param localTime {@code HH:MM}: only the minute counts
     * @param timezone  an IANA time zone, e.g. {@code Europe/London} or {@code UTC}
     * @return the schedule
     */
    public static Schedule hourly(String localTime, String timezone) {
        return new Schedule("hourly", localTime, timezone, null, null);
    }

    /**
     * Every day.
     *
     * @param localTime the time of day, {@code HH:MM} on a 24-hour clock, in {@code timezone}
     * @param timezone  an IANA time zone, e.g. {@code Europe/London}
     * @return the schedule
     */
    public static Schedule daily(String localTime, String timezone) {
        return new Schedule("daily", localTime, timezone, null, null);
    }

    /**
     * Monday to Friday.
     *
     * @param localTime the time of day, {@code HH:MM} on a 24-hour clock, in {@code timezone}
     * @param timezone  an IANA time zone, e.g. {@code Europe/London}
     * @return the schedule
     */
    public static Schedule weekdays(String localTime, String timezone) {
        return new Schedule("weekdays", localTime, timezone, null, null);
    }

    /**
     * Once a week.
     *
     * @param day       the day of the week
     * @param localTime the time of day, {@code HH:MM} on a 24-hour clock, in {@code timezone}
     * @param timezone  an IANA time zone, e.g. {@code Europe/London}
     * @return the schedule
     */
    public static Schedule weekly(DayOfWeek day, String localTime, String timezone) {
        Objects.requireNonNull(day, "day");
        return new Schedule("weekly", localTime, timezone, day.getValue() - 1, null);
    }

    /**
     * Once a month.
     *
     * @param dayOfMonth the day of the month, 1 to 31; past a short month's end, its last day
     * @param localTime  the time of day, {@code HH:MM} on a 24-hour clock, in {@code timezone}
     * @param timezone   an IANA time zone, e.g. {@code Europe/London}
     * @return the schedule
     */
    public static Schedule monthly(int dayOfMonth, String localTime, String timezone) {
        if (dayOfMonth < 1 || dayOfMonth > 31) {
            throw new IllegalArgumentException("dayOfMonth must be between 1 and 31");
        }
        return new Schedule("monthly", localTime, timezone, null, dayOfMonth);
    }

    /** @return {@code hourly}, {@code daily}, {@code weekdays}, {@code weekly} or {@code monthly} */
    public String cadence() {
        return cadence;
    }

    /** @return the time of day, {@code HH:MM} */
    public String localTime() {
        return localTime;
    }

    /** @return the IANA time zone */
    public String timezone() {
        return timezone;
    }

    /** @return for {@code weekly}, the day as the API counts it: 0 for Monday to 6 for Sunday; otherwise null */
    public Integer dayOfWeek() {
        return dayOfWeek;
    }

    /** @return for {@code monthly}, the day of the month; otherwise null */
    public Integer dayOfMonth() {
        return dayOfMonth;
    }

    /** The schedule as the API takes it: a day only for the cadence that has one. */
    Map<String, Object> toMap() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("cadence", cadence);
        out.put("local_time", localTime);
        out.put("timezone", timezone);
        if (dayOfWeek != null) {
            out.put("day_of_week", dayOfWeek);
        }
        if (dayOfMonth != null) {
            out.put("day_of_month", dayOfMonth);
        }
        return Collections.unmodifiableMap(out);
    }

    private static String checkTime(String localTime) {
        if (localTime == null || !localTime.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            throw new IllegalArgumentException("localTime must be HH:MM on a 24-hour clock, e.g. 07:30");
        }
        return localTime;
    }

    private static String checkZone(String timezone) {
        if (timezone == null || timezone.trim().isEmpty()) {
            throw new IllegalArgumentException("timezone must be an IANA time zone, e.g. Europe/London or UTC");
        }
        return timezone.trim();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Schedule && toMap().equals(((Schedule) other).toMap());
    }

    @Override
    public int hashCode() {
        return toMap().hashCode();
    }

    @Override
    public String toString() {
        return "Schedule" + toMap();
    }
}
