package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.PathNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultValueGenerator extends AbstractGenerator<Object> {

    private static final Map<Class<?>, Object> DEFAULT_VALUES = createDefaultValues();

    private static Map<Class<?>, Object> createDefaultValues() {
        Map<Class<?>, Object> map = new HashMap<>();
        map.put(boolean.class, false);
        map.put(char.class, '\0');
        map.put(byte.class, (byte) 0);
        map.put(short.class, (short) 0);
        map.put(int.class, 0);
        map.put(long.class, 0L);
        map.put(float.class, 0f);
        map.put(double.class, 0d);
        return Collections.unmodifiableMap(map);
    }

    @Override
    protected Object create(PathNode pathNode) {
        return nullOrDefaultValue(pathNode.getCalculatedNodeData().getClassType());
    }

    private static Object nullOrDefaultValue(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            return DEFAULT_VALUES.get(returnType);
        } else {
            return null;
        }
    }
}
