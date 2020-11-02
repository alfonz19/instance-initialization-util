package com.gmail.alfonz19.util.initialize.context.path;

public interface PathComponent extends Comparable<PathComponent>{
    Object getValue();

    PathComponentType getPathComponentType();

    boolean isArray();
}
