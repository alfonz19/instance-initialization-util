package com.gmail.alfonz19.util.initialize.context.path.builder;

import com.gmail.alfonz19.util.initialize.context.path.Path;

public class PathBuilder {
    private static final String SEPARATOR = Path.PATH_SEPARATOR_AS_STRING;
    private final StringBuilder stringBuilder = new StringBuilder();
    private static final String ARRAY_START = "[";
    private static final String ARRAY_END = "]";

    /**
     * Creates PathBuilder building {@link Path} from top-level.
     */
    public PathBuilder() {
        initRootPath();
    }

    /**
     * Creates PathBuilder building {@link Path} from given {@link Path}
     * @param path path to start with
     */
    public PathBuilder(Path path) {
        stringBuilder.append(path.getPathAsString());
    }

    public static PathBuilder root() {
        return new PathBuilder();
    }

    public PathBuilder addProperty(String property) {
        if (stringBuilder.length() == 1) {
            stringBuilder.append(property);
        } else {
            stringBuilder.append(SEPARATOR).append(property);
        }
        return this;
    }

    public PathBuilder addArrayIndex(int index) {
        if (index < 0) {
            throw new IllegalArgumentException();
        }

        stringBuilder.append(ARRAY_START).append(index).append(ARRAY_END);
        return this;
    }

    private void initRootPath() {
        if (stringBuilder.length() == 0) {
            stringBuilder.append(SEPARATOR);
        }
    }

    public PathBuilder addAssociativeArrayKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException();
        }

        initRootPath();

        stringBuilder.append(ARRAY_START).append("\"").append(key).append("\"").append(ARRAY_END);

        return this;
    }

    public String getPath() {
        return stringBuilder.toString();
    }
}
