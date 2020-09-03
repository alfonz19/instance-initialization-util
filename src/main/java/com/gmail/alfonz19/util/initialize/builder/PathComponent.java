package com.gmail.alfonz19.util.initialize.builder;

public interface PathComponent extends Comparable<PathComponent>{
    Object getValue();

    PathComponentType getPathComponentType();

    boolean isArray();
}
