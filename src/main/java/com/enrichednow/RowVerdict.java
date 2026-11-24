package com.enrichednow;

/** What is right or wrong with a formula row, as {@link FormulaRowService#feedback} says it. */
public enum RowVerdict {
    /** The row is right. */
    GOOD("good"),
    /** The row has the wrong company: give the right domain, or none to leave the company unmatched from now on. */
    WRONG_COMPANY("wrong_company"),
    /** The row has the wrong person or contact details. */
    WRONG_CONTACT("wrong_contact"),
    /** The row is right but of no use to you. */
    NOT_RELEVANT("not_relevant");

    private final String value;

    RowVerdict(String value) {
        this.value = value;
    }

    /** @return the verdict as the API writes it */
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
