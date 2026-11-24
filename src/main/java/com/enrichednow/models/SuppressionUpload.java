package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** What adding a CSV file to the suppression list did. */
public final class SuppressionUpload {
    private final long added;
    private final long already;
    private final List<Problem> invalid;

    @JsonCreator
    SuppressionUpload(@JsonProperty("added") long added,
                      @JsonProperty("already") long already,
                      @JsonProperty("invalid") List<Problem> invalid) {
        this.added = added;
        this.already = already;
        this.invalid = Values.list(invalid);
    }

    /** @return the entries new to the list */
    public long added() {
        return added;
    }

    /** @return the entries the list already held, left as they are */
    public long already() {
        return already;
    }

    /** @return each line that could not be read; the rest were added */
    public List<Problem> invalid() {
        return invalid;
    }

    @Override
    public String toString() {
        return "SuppressionUpload{added=" + added + ", already=" + already + ", invalid=" + invalid + '}';
    }

    /** A line of the file that could not be read. */
    public static final class Problem {
        private final int index;
        private final String message;

        @JsonCreator
        Problem(@JsonProperty("index") int index, @JsonProperty("message") String message) {
            this.index = index;
            this.message = message;
        }

        /** @return the line of the file, from 1 */
        public int index() {
            return index;
        }

        /** @return what is wrong with it */
        public String message() {
            return message;
        }

        @Override
        public String toString() {
            return "line " + index + ": " + message;
        }
    }
}
