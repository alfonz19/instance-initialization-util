package com.gmail.alfonz19.util.initialize.context.path.matcher;

import com.gmail.alfonz19.util.initialize.context.path.Path;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathMatcherBuilder {
    private static final String PATH_SEPARATOR = "\\.";
    private static final String NON_CAPTURING_START = "(?:";
    private static final String NON_CAPTURING_END = ")";
    private static final String OR = "|";
    private static final String ANY_PROPERTY_NAME = "[^\\.\\[]+";
    private static final String PRECEDED_BY_PATH_SEPARATOR = "(?<="+ PATH_SEPARATOR +")";
    private static final String ANY_ARRAY_INDEX = "\\[\\d+\\]";
    private static final String ANY_MAP_KEY = "\\[\"[^\"]+\"]";
    private static final String ANY_PROPERTY =
            NON_CAPTURING_START +
                PRECEDED_BY_PATH_SEPARATOR + ANY_PROPERTY_NAME +
                OR +
                PATH_SEPARATOR + ANY_PROPERTY_NAME +
            NON_CAPTURING_END;

    private static final String ANY_PATH_COMPONENT =
            NON_CAPTURING_START +
                ANY_ARRAY_INDEX +
                OR +
                ANY_MAP_KEY +
                OR +
                ANY_PROPERTY +
            NON_CAPTURING_END;

    private final StringBuilder stringBuilder = new StringBuilder();

    private PathMatcherBuilder() {
        stringBuilder.append("^\\.");
    }

    public static PathMatcherBuilder root() {
        return new PathMatcherBuilder();
    }

    public static PathMatcher matchAnyPropertyAnywhere() {
        return root().addAnySubPath().addAnyProperty().build();
    }

    public PathMatcherBuilder addProperty(String property) {
        validatePropertyName(property);

        stringBuilder.append(createPropertyMatchingRegex(property));

        return this;
    }

    public PathMatcherBuilder addPropertyRegex(String property) {
        stringBuilder.append(createPropertyMatchingRegex(property));

        return this;
    }

    private String createPropertyMatchingRegex(String property) {
        return NON_CAPTURING_START + PRECEDED_BY_PATH_SEPARATOR + property
                + OR
                + PATH_SEPARATOR + property + NON_CAPTURING_END;
    }

    public PathMatcherBuilder addAnyProperty() {
        stringBuilder.append(ANY_PROPERTY);
        return this;
    }

    public PathMatcherBuilder addOptionalProperty() {
        stringBuilder.append(
                NON_CAPTURING_START
                    +NON_CAPTURING_START
                            +PRECEDED_BY_PATH_SEPARATOR+ANY_PROPERTY_NAME
                            +OR
                            +PATH_SEPARATOR+ANY_PROPERTY_NAME
                    +NON_CAPTURING_END
                +OR
                +NON_CAPTURING_END
        );
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
        validateKey(key);

        stringBuilder.append("\\[\"").append(key).append("\"\\]");

        return this;
    }

    public PathMatcherBuilder addAnyArrayIndex() {
        stringBuilder.append(ANY_ARRAY_INDEX);
        return this;
    }

    public PathMatcherBuilder addAnyAssociativeArray() {
        stringBuilder.append(ANY_MAP_KEY);
        return this;
    }

    public PathMatcherBuilder addAnyPathComponent() {
        stringBuilder.append(ANY_PATH_COMPONENT);

        return this;
    }

    public PathMatcherBuilder addAnyOptionalPathComponent() {
        stringBuilder.append(
                NON_CAPTURING_START +
                        ANY_PATH_COMPONENT +
                        OR +
                NON_CAPTURING_END
        );

        return this;
    }

    public PathMatcherBuilder addAnySubPath() {
        stringBuilder.append(ANY_PATH_COMPONENT).append("*");
        return this;
    }

    private void validatePropertyName(String property) {
        if (property == null || property.isEmpty() || property.contains(".")) {
            throw new IllegalArgumentException();
        }
    }

    private void validateKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public PathMatcher build() {
        stringBuilder.append("$");
        Pattern pattern = Pattern.compile(stringBuilder.toString());
        return new PathMatcherImpl(pattern);
    }

    private static class PathMatcherImpl implements PathMatcher{
        private final Pattern pattern;
        private Matcher matcher;

        private PathMatcherImpl(Pattern pattern) {
            this.pattern = pattern;
        }

        @Override
        public boolean matches(String input) {
            if (matcher == null) {
                matcher = pattern.matcher(input);
            } else {
                matcher.reset(input);
            }

            return matcher.matches();
        }

        @Override
        public boolean matches(Path path) {
            return this.matches(path.getPathAsString());
        }

        @Override
        public String describe() {
            //TODO MMUCHA: implement.
            return "Unimplemented: description of path matcher content.";
        }
    }

}
