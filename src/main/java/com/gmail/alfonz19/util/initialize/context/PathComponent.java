package com.gmail.alfonz19.util.initialize.context;

public interface PathComponent extends Comparable<PathComponent>{
    Object getValue();

    PathComponentType getPathComponentType();

    boolean isArray();
}
