package com.gmail.alfonz19.util.initialize.builder;

public class MapKeyPathComponent extends AbstractPathComponent<String> {
    public MapKeyPathComponent(String key) {
        super(key, PathComponentType.ARRAY_INDEX);
    }

    @Override
    public int compareTo(PathComponent other) {
        int a = this.getPathComponentType().compareTo(other.getPathComponentType());
        if (a != 0) {
            return a;
        }
        return this.getValue().compareTo((String)other.getValue());
    }
}
