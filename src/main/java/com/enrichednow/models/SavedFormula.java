package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * A formula saved with its parameters and caps, to run again by hand or on a schedule. Its runs deliver only rows it
 * has not delivered before, so polling its rows gives what is new.
 */
public final class SavedFormula {
    private final String id;
    private final String formulaId;
    private final String version;
    private final int major;
    private final String name;
    private final boolean nameAuto;
    private final Map<String, Object> params;
    private final long unitCap;
    private final long rowCap;
    private final Long monthlyUnitCap;
    private final String status;
    private final String pausedReason;
    private final String runsWithKey;
    private final String notifyMode;
    private final Schedule schedule;
    private final int consecutiveFailures;
    private final int emptyRuns;
    private final List<Note> notes;
    private final CostEstimate estimate;
    private final LastRun lastRun;
    private final long units30d;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant archivedAt;

    @JsonCreator
    SavedFormula(@JsonProperty("id") String id,
                 @JsonProperty("formula_id") String formulaId,
                 @JsonProperty("version") String version,
                 @JsonProperty("major") int major,
                 @JsonProperty("name") String name,
                 @JsonProperty("name_auto") boolean nameAuto,
                 @JsonProperty("params") Map<String, Object> params,
                 @JsonProperty("unit_cap") long unitCap,
                 @JsonProperty("row_cap") long rowCap,
                 @JsonProperty("monthly_unit_cap") Long monthlyUnitCap,
                 @JsonProperty("status") String status,
                 @JsonProperty("paused_reason") String pausedReason,
                 @JsonProperty("runs_with_key") String runsWithKey,
                 @JsonProperty("notify") String notifyMode,
                 @JsonProperty("schedule") Schedule schedule,
                 @JsonProperty("consecutive_failures") int consecutiveFailures,
                 @JsonProperty("empty_runs") int emptyRuns,
                 @JsonProperty("notes") List<Note> notes,
                 @JsonProperty("estimate") CostEstimate estimate,
                 @JsonProperty("last_run") LastRun lastRun,
                 @JsonProperty("units_30d") long units30d,
                 @JsonProperty("created_at") String createdAt,
                 @JsonProperty("updated_at") String updatedAt,
                 @JsonProperty("archived_at") String archivedAt) {
        this.id = id;
        this.formulaId = formulaId;
        this.version = version;
        this.major = major;
        this.name = name;
        this.nameAuto = nameAuto;
        this.params = Values.map(params);
        this.unitCap = unitCap;
        this.rowCap = rowCap;
        this.monthlyUnitCap = monthlyUnitCap;
        this.status = status;
        this.pausedReason = pausedReason;
        this.runsWithKey = runsWithKey;
        this.notifyMode = notifyMode;
        this.schedule = schedule;
        this.consecutiveFailures = consecutiveFailures;
        this.emptyRuns = emptyRuns;
        this.notes = Values.list(notes);
        this.estimate = estimate;
        this.lastRun = lastRun;
        this.units30d = units30d;
        this.createdAt = Values.instant(createdAt);
        this.updatedAt = Values.instant(updatedAt);
        this.archivedAt = Values.instant(archivedAt);
    }

    /** @return the saved formula's id, {@code sf_…} */
    public String id() {
        return id;
    }

    /** @return the formula it runs */
    public String formulaId() {
        return formulaId;
    }

    /** @return the formula version its runs use now: the newest of its {@link #major()}; null when that is no longer offered */
    public String version() {
        return version;
    }

    /** @return the major version of the formula it is pinned to */
    public int major() {
        return major;
    }

    /** @return its name */
    public String name() {
        return name;
    }

    /** @return whether its name is made from its parameters, and follows them when they change; a rename stops that */
    public boolean nameAuto() {
        return nameAuto;
    }

    /** @return the parameters its runs use, the formula's defaults filled in */
    public Map<String, Object> params() {
        return params;
    }

    /** @return the most one run may spend */
    public long unitCap() {
        return unitCap;
    }

    /** @return the most rows one run may return */
    public long rowCap() {
        return rowCap;
    }

    /** @return the most its runs may spend in a calendar month (UTC), or null for no such cap */
    public Long monthlyUnitCap() {
        return monthlyUnitCap;
    }

    /** @return {@code active}, {@code paused} or {@code archived} */
    public String status() {
        return status;
    }

    /** @return whether it is {@code active} */
    public boolean isActive() {
        return "active".equals(status);
    }

    /** @return whether it is {@code paused}: no scheduled run starts, it can still be run by hand */
    public boolean isPaused() {
        return "paused".equals(status);
    }

    /** @return whether it is {@code archived}: it does not run, and can only be duplicated */
    public boolean isArchived() {
        return "archived".equals(status);
    }

    /**
     * @return why it is paused: {@code manual}, {@code failures} (3 scheduled runs in a row failed), {@code balance},
     *     {@code attestation} or {@code key}; null when it is not
     */
    public String pausedReason() {
        return pausedReason;
    }

    /** @return the API key its schedule runs with, whose limits apply to its scheduled runs; null when a person set it */
    public String runsWithKey() {
        return runsWithKey;
    }

    /**
     * The API's {@code notify}: when mail is sent about new rows of a scheduled run.
     *
     * @return {@code each_run}, {@code digest} (daily) or {@code none}
     */
    public String notifyMode() {
        return notifyMode;
    }

    /** @return its schedule, or null when it runs only by hand */
    public Schedule schedule() {
        return schedule;
    }

    /** @return its scheduled runs in a row that failed */
    public int consecutiveFailures() {
        return consecutiveFailures;
    }

    /** @return its scheduled runs in a row that found no new rows */
    public int emptyRuns() {
        return emptyRuns;
    }

    /** @return what the API wants you to know about it, e.g. a source switched off for now */
    public List<Note> notes() {
        return notes;
    }

    /** @return about what one run costs, as estimated when it was last changed; or null */
    public CostEstimate estimate() {
        return estimate;
    }

    /** @return its latest run, or null before the first */
    public LastRun lastRun() {
        return lastRun;
    }

    /** @return the units its runs were charged in the last 30 days */
    public long units30d() {
        return units30d;
    }

    /** @return when it was saved */
    public Instant createdAt() {
        return createdAt;
    }

    /** @return when it last changed */
    public Instant updatedAt() {
        return updatedAt;
    }

    /** @return when it was archived, or null */
    public Instant archivedAt() {
        return archivedAt;
    }

    @Override
    public String toString() {
        return "SavedFormula{id=" + id + ", formulaId=" + formulaId + ", name=" + name + ", status=" + status + ", schedule=" + schedule + '}';
    }

    /** When a saved formula runs by itself. */
    public static final class Schedule {
        private final String cadence;
        private final String localTime;
        private final String timezone;
        private final Integer dayOfWeek;
        private final Integer dayOfMonth;
        private final Instant nextFireAt;

        @JsonCreator
        Schedule(@JsonProperty("cadence") String cadence,
                 @JsonProperty("local_time") String localTime,
                 @JsonProperty("timezone") String timezone,
                 @JsonProperty("day_of_week") Integer dayOfWeek,
                 @JsonProperty("day_of_month") Integer dayOfMonth,
                 @JsonProperty("next_fire_at") String nextFireAt) {
            this.cadence = cadence;
            this.localTime = localTime;
            this.timezone = timezone;
            this.dayOfWeek = dayOfWeek;
            this.dayOfMonth = dayOfMonth;
            this.nextFireAt = Values.instant(nextFireAt);
        }

        /** @return {@code hourly}, {@code daily}, {@code weekdays}, {@code weekly} or {@code monthly} */
        public String cadence() {
            return cadence;
        }

        /** @return the time of day it runs, {@code HH:MM} in {@link #timezone()}; for {@code hourly}, only the minute counts */
        public String localTime() {
            return localTime;
        }

        /** @return the IANA time zone of {@link #localTime()}, e.g. {@code Europe/London} */
        public String timezone() {
            return timezone;
        }

        /** @return for {@code weekly}, the day it runs, 0 for Monday to 6 for Sunday; otherwise null */
        public Integer dayOfWeek() {
            return dayOfWeek;
        }

        /** @return for {@code monthly}, the day of the month it runs (past a short month's end, its last day); otherwise null */
        public Integer dayOfMonth() {
            return dayOfMonth;
        }

        /** @return its next run, or null while the saved formula is paused or archived */
        public Instant nextFireAt() {
            return nextFireAt;
        }

        @Override
        public String toString() {
            return "Schedule{cadence=" + cadence + ", localTime=" + localTime + ", timezone=" + timezone + ", nextFireAt=" + nextFireAt + '}';
        }
    }

    /** A note on a saved formula. */
    public static final class Note {
        private final String code;
        private final String message;
        private final Instant at;
        private final Map<String, Object> values;

        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        Note(Map<String, Object> values) {
            this.values = Values.map(values);
            this.code = Values.text(values, "code");
            this.message = Values.text(values, "message");
            this.at = Values.instant(Values.text(values, "at"));
        }

        /**
         * @return its code: {@code source_disabled} (a source it reads is switched off for now; {@code scraper} names it)
         *     or {@code widen} (its last 14 scheduled runs found nothing new)
         */
        public String code() {
            return code;
        }

        /** @return what it says, for a person */
        public String message() {
            return message;
        }

        /** @return when it was written */
        public Instant at() {
            return at;
        }

        /** @return everything the API wrote of it, {@code code}, {@code message} and {@code at} included */
        public Map<String, Object> values() {
            return values;
        }

        @Override
        public String toString() {
            return "Note{code=" + code + ", message=" + message + '}';
        }
    }

    /** The latest run of a saved formula, in short. */
    public static final class LastRun {
        private final String id;
        private final String rawStatus;
        private final RunStatus status;
        private final String reason;
        private final long rowsNew;
        private final Instant createdAt;
        private final Instant finishedAt;

        @JsonCreator
        LastRun(@JsonProperty("id") String id,
                @JsonProperty("status") String status,
                @JsonProperty("reason") String reason,
                @JsonProperty("rows_new") long rowsNew,
                @JsonProperty("created_at") String createdAt,
                @JsonProperty("finished_at") String finishedAt) {
            this.id = id;
            this.rawStatus = status;
            this.status = RunStatus.fromValue(status);
            this.reason = reason;
            this.rowsNew = rowsNew;
            this.createdAt = Values.instant(createdAt);
            this.finishedAt = Values.instant(finishedAt);
        }

        /** @return the run's id: {@code client.formulaRuns().get(id)} has the rest */
        public String id() {
            return id;
        }

        /** @return its status */
        public RunStatus status() {
            return status;
        }

        /** @return its status exactly as the API wrote it */
        public String rawStatus() {
            return rawStatus;
        }

        /** @return why it is not plainly {@code ok}, or null */
        public String reason() {
            return reason;
        }

        /** @return the new rows it delivered */
        public long rowsNew() {
            return rowsNew;
        }

        /** @return when it was created */
        public Instant createdAt() {
            return createdAt;
        }

        /** @return when it ended, or null while it is going */
        public Instant finishedAt() {
            return finishedAt;
        }

        @Override
        public String toString() {
            return "LastRun{id=" + id + ", status=" + rawStatus + ", rowsNew=" + rowsNew + '}';
        }
    }
}
