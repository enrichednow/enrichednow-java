package com.enrichednow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** One question a formula asks before it runs: a parameter of its Basics ({@link Formula#basics()}). */
public final class FormulaInput {
    private final String name;
    private final String title;
    private final String type;
    private final boolean required;
    private final String help;

    @JsonCreator
    FormulaInput(@JsonProperty("name") String name,
                 @JsonProperty("title") String title,
                 @JsonProperty("type") String type,
                 @JsonProperty("required") boolean required,
                 @JsonProperty("help") String help) {
        this.name = name;
        this.title = title;
        this.type = type;
        this.required = required;
        this.help = help;
    }

    /** @return the key in the parameters, e.g. {@code job_titles} */
    public String name() {
        return name;
    }

    /** @return what the question is called, e.g. {@code Job titles} */
    public String title() {
        return title;
    }

    /** @return {@code string}, {@code integer}, {@code boolean}, {@code object}, or a list: {@code list of strings} */
    public String type() {
        return type;
    }

    /** @return whether a run is refused without it; otherwise it has a default */
    public boolean required() {
        return required;
    }

    /** @return one sentence on what to give, or null */
    public String help() {
        return help;
    }

    @Override
    public String toString() {
        return "FormulaInput{name=" + name + ", type=" + type + ", required=" + required + '}';
    }
}
