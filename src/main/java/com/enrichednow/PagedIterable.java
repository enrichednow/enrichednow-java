package com.enrichednow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A list that pages itself: iterate it and the next page is fetched when the current one runs out.
 *
 * <pre>{@code
 * for (Run run : client.runs().list()) { ... }                 // every run, page by page
 * client.runs().list().stream().limit(50).forEach(...);        // stops fetching after 50
 * for (Page<Run> page : client.runs().list().pages()) { ... }  // a page at a time
 * }</pre>
 *
 * <p>Nothing is requested until iteration starts, and each iteration starts again from the first page. A request that
 * fails throws from {@code hasNext()} / {@code next()}, as any call of the SDK would.
 *
 * @param <T> what the list holds
 */
public final class PagedIterable<T> implements Iterable<T> {
    private final Supplier<? extends Page<T>> first;

    PagedIterable(Supplier<? extends Page<T>> first) {
        this.first = first;
    }

    /**
     * Fetches the first page.
     *
     * @return the first page; ask it for the {@linkplain Page#nextPage() next}
     */
    public Page<T> firstPage() {
        return first.get();
    }

    /** @return the pages, each fetched when it is reached */
    public Iterable<Page<T>> pages() {
        return () -> new Iterator<Page<T>>() {
            private Page<T> current;
            private boolean started;

            @Override
            public boolean hasNext() {
                return !started || current.hasNextPage();
            }

            @Override
            public Page<T> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                current = started ? current.nextPage() : first.get();
                started = true;
                return current;
            }
        };
    }

    @Override
    public Iterator<T> iterator() {
        Iterator<Page<T>> pages = pages().iterator();
        return new Iterator<T>() {
            private Iterator<T> items = java.util.Collections.emptyIterator();

            @Override
            public boolean hasNext() {
                while (!items.hasNext() && pages.hasNext()) {
                    items = pages.next().items().iterator();
                }
                return items.hasNext();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return items.next();
            }
        };
    }

    /** @return the items as a lazy, sequential stream; pages are fetched as the stream is consumed */
    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Fetches every page.
     *
     * @return all the items, in the API's order
     */
    public List<T> toList() {
        List<T> all = new ArrayList<>();
        for (T item : this) {
            all.add(item);
        }
        return all;
    }
}
