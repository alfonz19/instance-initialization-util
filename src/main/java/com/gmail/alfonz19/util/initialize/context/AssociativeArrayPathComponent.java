package com.gmail.alfonz19.util.initialize.context;

public class AssociativeArrayPathComponent extends AbstractPathComponent<String> {
    public AssociativeArrayPathComponent(String key) {
        super(key, PathComponentType.MAP_KEY);
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
