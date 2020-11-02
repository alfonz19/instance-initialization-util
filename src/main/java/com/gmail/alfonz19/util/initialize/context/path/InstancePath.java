package com.gmail.alfonz19.util.initialize.context.path;

import com.gmail.alfonz19.util.initialize.context.path.builder.PathBuilder;
import lombok.EqualsAndHashCode;

import java.beans.PropertyDescriptor;
import java.util.Objects;

@EqualsAndHashCode(of = "path")
public class InstancePath implements Path {

    private final String path;
    private final PathComponents pathComponents;
    public static final Path ROOT_PATH = new InstancePath();

    /**
     * Creates root path;
     */
    private InstancePath() {
        this(PATH_SEPARATOR_AS_STRING);
    }

    public InstancePath(String path) {
        this(path, new PathComponents(path));
    }

    private InstancePath(String path, PathComponents pathComponents) {
        this.path = path;
        this.pathComponents = pathComponents;
    }

    @Override
    public boolean isRootPath() {
        return Objects.equals(PATH_SEPARATOR_AS_STRING, path);
    }

    @Override
    public String getPathAsString() {
        if (isRootPath()) {
            return PATH_SEPARATOR_AS_STRING;
        }

        return path;
    }

    @Override
    public int getPathLength() {
        return getPathComponents().getNumberOfComponents();
    }

    @Override
    public Path createSubPathTraversingProperty(PropertyDescriptor propertyDescriptor) {
        String propertyName = Objects.requireNonNull(propertyDescriptor).getName();
        if (Objects.requireNonNull(propertyName).contains(PATH_SEPARATOR_AS_STRING)) {
            throw new IllegalArgumentException();
        }

        return new InstancePath(
                new PathBuilder(this).addProperty(propertyName).getPath(),
                pathComponents.appendPropertyPathComponent(propertyName));
    }

    @Override
    public Path createSubPathTraversingArray(int index) {
        return new InstancePath(
                new PathBuilder(this).addArrayIndex(index).getPath(),
                pathComponents.appendArrayIndexPathComponent(index));
    }

    @Override
    public Path createSubPathTraversingMap(String key) {
        return new InstancePath(
                new PathBuilder(this).addAssociativeArrayKey(key).getPath(),
                pathComponents.appendAssociativeArrayPathComponent(key));
    }

    @Override
    public PathComponents getPathComponents() {
        return pathComponents;
    }

    @Override
    public String toString() {
        return getPathAsString();
    }
}
