package com.enrichednow;

import java.util.List;
import java.util.function.Function;

/**
 * One page of a run's records.
 *
 * @param <T> the type the records were read as
 */
public final class RecordPage<T> extends Page<T> {
    private final boolean expired;

    RecordPage(List<T> items, String nextCursor, boolean expired, Transport transport,
               Function<String, ? extends ApiRequest<? extends Page<T>>> next) {
        super(items, nextCursor, transport, next);
        this.expired = expired;
    }

    /**
     * Records are kept for 7 days after the run ended. After that the list is empty and this is {@code true}; the run
     * itself stays, with its record count.
     *
     * @return whether the records are gone
     */
    public boolean expired() {
        return expired;
    }

    @Override
    public RecordPage<T> nextPage() {
        return (RecordPage<T>) super.nextPage();
    }
}
