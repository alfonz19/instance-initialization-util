package com.gmail.alfonz19.util.initialize.context;

import com.gmail.alfonz19.util.initialize.util.TypeVariableAssignments;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CalculatedNodeData {
    @Getter
    private final Class<?> classType;

    @Getter
    private final Type genericClassType;
    @Getter
    private final TypeVariableAssignments typeVariableAssignment;

    public CalculatedNodeData(Class<?> instanceClassType) {
        this(instanceClassType, instanceClassType, TypeVariableAssignments.NO_TYPE_VARIABLE_ASSIGNMENTS);
    }

    public CalculatedNodeData(Class<?> classType, Type genericClassType, TypeVariableAssignments typeVariableAssignment) {
        this.classType = classType;
        this.genericClassType = genericClassType;
        this.typeVariableAssignment = typeVariableAssignment;
    }

    public boolean representsParameterizedType() {
        return genericClassType instanceof ParameterizedType;
    }
}
