package com.gmail.alfonz19.util.initialize.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum DefaultPrimitiveValue {
    INSTANCE;

    private final Map<Class<?>, Object> defaultValues;

    DefaultPrimitiveValue() {
        Map<Class<?>, Object> map = new HashMap<>();
        map.put(boolean.class, false);
        map.put(char.class, '\0');
        map.put(byte.class, (byte) 0);
        map.put(short.class, (short) 0);
        map.put(int.class, 0);
        map.put(long.class, 0L);
        map.put(float.class, 0f);
        map.put(double.class, 0d);
        defaultValues = Collections.unmodifiableMap(map);
    }

    public Object get(Class<?> returnType) {
        return defaultValues.get(returnType);
    }
}
