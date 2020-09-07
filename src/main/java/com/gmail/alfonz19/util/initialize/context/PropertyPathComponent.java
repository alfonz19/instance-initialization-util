package com.gmail.alfonz19.util.initialize.context;

public class PropertyPathComponent extends AbstractPathComponent<String> {

    public PropertyPathComponent(String value) {
        super(value, PathComponentType.PROPERTY);
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
