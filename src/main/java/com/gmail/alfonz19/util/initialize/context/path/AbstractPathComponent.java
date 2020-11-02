package com.gmail.alfonz19.util.initialize.context.path;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public abstract class AbstractPathComponent implements PathComponent {

    @Getter
    private final Object value;
    @Getter
    private final PathComponentType pathComponentType;

    public AbstractPathComponent(Object value, PathComponentType pathComponentType) {

        this.value = value;
        this.pathComponentType = pathComponentType;
    }

    @Override
    public boolean isArray() {
        return this.pathComponentType == PathComponentType.ARRAY_INDEX;
    }
}
