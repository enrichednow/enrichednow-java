package com.enrichednow;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * One page of a list, with the cursor of the page after it.
 *
 * <p>A cursor is an opaque string: pass it back unchanged, do not build or edit one.
 *
 * @param <T> what the list holds
 */
public class Page<T> {
    private final List<T> items;
    private final String nextCursor;
    private final Transport transport;
    private final Function<String, ? extends ApiRequest<? extends Page<T>>> next;

    Page(List<T> items, String nextCursor, Transport transport, Function<String, ? extends ApiRequest<? extends Page<T>>> next) {
        this.items = Collections.unmodifiableList(items);
        this.nextCursor = nextCursor;
        this.transport = transport;
        this.next = next;
    }

    /** @return the items of this page, in the API's order */
    public List<T> items() {
        return items;
    }

    /** @return the cursor of the next page, or {@code null} when this is the last one */
    public String nextCursor() {
        return nextCursor;
    }

    /** @return whether there is a page after this one */
    public boolean hasNextPage() {
        return nextCursor != null;
    }

    /**
     * Fetches the page after this one, with the same filters.
     *
     * @return the next page
     * @throws NoSuchElementException when this is the last page
     */
    public Page<T> nextPage() {
        return transport.execute(nextRequest());
    }

    /**
     * Fetches the page after this one without blocking.
     *
     * @return the next page, later
     * @throws NoSuchElementException when this is the last page
     */
    public CompletableFuture<Page<T>> nextPageAsync() {
        return transport.executeAsync(nextRequest()).thenApply(page -> page);
    }

    ApiRequest<? extends Page<T>> nextRequest() {
        if (nextCursor == null) {
            throw new NoSuchElementException("This is the last page.");
        }
        return next.apply(nextCursor);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{items=" + items.size() + ", nextCursor=" + nextCursor + '}';
    }
}
