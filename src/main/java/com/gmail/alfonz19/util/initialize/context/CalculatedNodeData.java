package com.gmail.alfonz19.util.initialize.context;

import lombok.Getter;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.Map;

@Getter
public class CalculatedNodeData {
    private final Class<?> classType;
    private final Map<TypeVariable<?>, Type> typeVariableAssignment;

    public CalculatedNodeData(Class<?> instanceClassType) {
        this(instanceClassType, Collections.emptyMap());
    }

    public CalculatedNodeData(Class<?> classType, Map<TypeVariable<?>, Type> typeVariableAssignment) {
        this.classType = classType;
        this.typeVariableAssignment = typeVariableAssignment;
    }
}
