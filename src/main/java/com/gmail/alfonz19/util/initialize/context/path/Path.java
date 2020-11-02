package com.gmail.alfonz19.util.initialize.context.path;

import java.beans.PropertyDescriptor;

/**
 * Path represents 'path' along which we get from root Class (or instance), to currently processed Class (or instance)
 */
@SuppressWarnings({"squid:S1214", "unused"})    //constants in interface (I don't have better place for them yet), unused methods.
public interface Path {
    char PATH_SEPARATOR = '.';
    String PATH_SEPARATOR_AS_STRING = Character.toString(PATH_SEPARATOR);

    boolean isRootPath();
    String getPathAsString();
    int getPathLength();
    PathComponents getPathComponents();

    Path createSubPathTraversingProperty(PropertyDescriptor propertyDescriptor);
    Path createSubPathTraversingArray(int index);
    Path createSubPathTraversingMap(String key);
}
