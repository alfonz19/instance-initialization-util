package com.gmail.alfonz19.util.initialize.context;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public abstract class AbstractPathComponent<T extends Comparable<T>> implements PathComponent {

    @Getter
    private final T value;
    @Getter
    private final PathComponentType pathComponentType;

    public AbstractPathComponent(T value, PathComponentType pathComponentType) {

        this.value = value;
        this.pathComponentType = pathComponentType;
    }

    @Override
    public boolean isArray() {
        return this.pathComponentType == PathComponentType.ARRAY_INDEX;
    }
}
