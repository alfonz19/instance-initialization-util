package com.gmail.alfonz19.util.initialize.builder;

public class ArryIndexPathComponent extends AbstractPathComponent<Integer> {

    public ArryIndexPathComponent(Integer value) {
        super(value, PathComponentType.ARRAY_INDEX);
    }

    @Override
    public int compareTo(PathComponent other) {
        int a = this.getPathComponentType().compareTo(other.getPathComponentType());
        if (a != 0) {
            return a;
        }
        return this.getValue().compareTo((Integer)other.getValue());
    }
}
