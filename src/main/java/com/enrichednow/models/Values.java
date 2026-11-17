package com.enrichednow.models;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** How the models read times and copy collections. */
final class Values {
    private Values() {
    }

    /** An RFC 3339 time as an instant; a time without an offset is read as UTC. */
    static Instant instant(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            return OffsetDateTime.parse(text).toInstant();
        } catch (DateTimeParseException withoutOffset) {
            try {
                return LocalDateTime.parse(text).toInstant(ZoneOffset.UTC);
            } catch (DateTimeParseException dateOnly) {
                return LocalDate.parse(text).atStartOfDay().toInstant(ZoneOffset.UTC);
            }
        }
    }

    static LocalDate date(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        return LocalDate.parse(text.length() > 10 ? text.substring(0, 10) : text);
    }

    static <T> List<T> list(List<T> items) {
        return items == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<>(items));
    }

    static Map<String, Object> map(Map<String, Object> items) {
        return items == null ? Collections.emptyMap() : Collections.unmodifiableMap(new LinkedHashMap<>(items));
    }

    static List<Instant> instants(List<String> items) {
        if (items == null) {
            return Collections.emptyList();
        }
        List<Instant> out = new ArrayList<>(items.size());
        for (String item : items) {
            out.add(instant(item));
        }
        return Collections.unmodifiableList(out);
    }
}
