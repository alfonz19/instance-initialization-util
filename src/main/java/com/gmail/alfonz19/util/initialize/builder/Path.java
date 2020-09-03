package com.gmail.alfonz19.util.initialize.builder;

import java.beans.PropertyDescriptor;
import java.util.Objects;

/**
 * Path represents 'path' along which we get from root Class (or instance), to currently processed Class (or instance)
 */
@SuppressWarnings({"squid:S1214", "unused"})    //constants in interface (I don't have better place for them yet), unused methods.
public interface Path {
    String PATH_SEPARATOR = ".";
    String ROOT_PATH = ".";
    String ROOT_PATH_STRING = ".";

    String getPathAsString();

    InstancePath createSubPathTraversingProperty(PropertyDescriptor propertyDescriptor);
    InstancePath createSubPathTraversingArray(int index);
    InstancePath createSubPathTraversingMap(String key);

    default PathComponents getPathComponents() {
        return new PathComponents(getPathAsString());
    }

    boolean isRootPath();

    default String createArrayIndexString(int index) {
        return "[" + index + "]";
    }

    default String createMapKeyString(String key) {
        return "[" + Objects.requireNonNull(key) + "]";
    }

    class InstancePath implements Path {

        private final String path;

        /**
         *  Creates root path;
         */
        public InstancePath() {
            this(ROOT_PATH);
        }

        private InstancePath(String path) {
            this.path = path;
        }

        @Override
        public boolean isRootPath() {
            return Objects.equals(ROOT_PATH, path);
        }

        @Override
        public String getPathAsString() {
            if (isRootPath()) {
                return ROOT_PATH_STRING;
            }

            return path;
        }

        @Override
        public InstancePath createSubPathTraversingProperty(PropertyDescriptor propertyDescriptor) {
            String propertyName = Objects.requireNonNull(propertyDescriptor).getName();
            if (Objects.requireNonNull(propertyName).contains(PATH_SEPARATOR)) {
                throw new IllegalArgumentException();
            }

            return new InstancePath(isRootPath() ? this.path + propertyName : this.path + PATH_SEPARATOR + propertyName);
        }

        @Override
        public InstancePath createSubPathTraversingArray(int index) {
            return new InstancePath(isRootPath() ? createArrayIndexString(index) : this.path + createArrayIndexString(index));
        }

        @Override
        public InstancePath createSubPathTraversingMap(String key) {
            return new InstancePath(isRootPath() ? createMapKeyString(key) : this.path + createMapKeyString(key));
        }

        @Override
        public String toString() {
            return getPathAsString();
        }
    }

}
