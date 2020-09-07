package com.gmail.alfonz19.util.initialize.context;

import java.util.regex.Pattern;

public class PathMatcherBuilder {
    private static final String PATH_SEPARATOR_REGEX = "\\.";
    private final StringBuilder stringBuilder = new StringBuilder();
    private final int initialLength;

    public PathMatcherBuilder() {
        stringBuilder.append("^\\.");
        initialLength = stringBuilder.length();
    }

    public static PathMatcherBuilder root() {
        return new PathMatcherBuilder();
    }

    public PathMatcherBuilder addProperty(String property) {
        if (stringBuilder.length() == initialLength) {
            stringBuilder.append(property);
        } else {
            stringBuilder.append(PATH_SEPARATOR_REGEX).append(property);
        }
        return this;
    }

    public PathMatcherBuilder addAnyProperty() {
        if (stringBuilder.length() == initialLength) {
            stringBuilder.append("[^\\.\\[]+");
        } else {
            stringBuilder.append(PATH_SEPARATOR_REGEX).append("[^\\.\\[]*");
        }
        return this;
    }


    public PathMatcherBuilder addArrayIndex(int index) {
        if (index < 0) {
            throw new IllegalArgumentException();
        }

        stringBuilder.append("\\[").append(index).append("\\]");
        return this;
    }

    public PathMatcherBuilder addAssociativeArrayKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException();
        }

        stringBuilder.append("\\[\"").append(key).append("\"\\]");

        return this;
    }

    public Pattern getPattern() {
        stringBuilder.append("$");
        return Pattern.compile(stringBuilder.toString());
    }

    public PathMatcherBuilder addAnyArrayIndex() {
        stringBuilder.append("\\[\\d+\\]");
        return this;
    }

    public PathMatcherBuilder addAnyAssociativeArray() {
        stringBuilder.append("\\[\"[^\"]+\"]");
        return this;
    }

    public PathMatcherBuilder addAnyPathComponent() {
        if (stringBuilder.length() == initialLength) {
            //non-capturing group of or-red non-capturing groups, in order: array-index, map-key, property.
            //two regexes differs by leading dot in property matcher.
            stringBuilder.append("(?:(?:\\[\\d+\\])|(?:\\[\"[^\"]+\"\\])|(?:[^\\.\\[]+))");
        } else {
            stringBuilder.append("(?:(?:\\[\\d+\\])|(?:\\[\"[^\"]+\"\\])|(?:\\.[^\\.\\[]+))");
        }

        return this;
    }

    public PathMatcherBuilder addAnySubPath() {
        if (stringBuilder.length() == initialLength) {
            addAnyPathComponent();
            stringBuilder.append("?");
            addAnyPathComponent();
            stringBuilder.append("*");
        } else {
            addAnyPathComponent();
            stringBuilder.append("*");
        }
        return this;
    }
}
