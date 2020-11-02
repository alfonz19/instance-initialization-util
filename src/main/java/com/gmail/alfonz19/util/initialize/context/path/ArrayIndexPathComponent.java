package com.gmail.alfonz19.util.initialize.context.path;

public class ArrayIndexPathComponent extends AbstractPathComponent {

    public ArrayIndexPathComponent(Integer value) {
        super(value, PathComponentType.ARRAY_INDEX);
    }

    @Override
    public int compareTo(PathComponent other) {
        int a = this.getPathComponentType().compareTo(other.getPathComponentType());
        if (a != 0) {
            return a;
        }
        return ((Integer)this.getValue()).compareTo((Integer)other.getValue());
    }
}
