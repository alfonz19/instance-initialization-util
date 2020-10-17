package com.gmail.alfonz19.util.initialize.util;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.Map;

public class TypeVariableAssignments {
    private final Map<Class<?>, Map<TypeVariable<?>, Type>> typeVariableAssignmentPerClass;
    public static final TypeVariableAssignments NO_TYPE_VARIABLE_ASSIGNMENTS =
            new TypeVariableAssignments(Collections.emptyMap());


    public TypeVariableAssignments(Map<Class<?>, Map<TypeVariable<?>, Type>> typeVariableAssignmentPerClass) {
        this.typeVariableAssignmentPerClass = typeVariableAssignmentPerClass;
    }

    public Map<TypeVariable<?>, Type> getAssignmentsForClassInHierarchy(Class<?> declaringClass) {
        return Collections.unmodifiableMap(typeVariableAssignmentPerClass.getOrDefault(declaringClass,
                Collections.emptyMap()));
    }

    public boolean containsAssignmentInfoFor(Class<?> declaringClass, TypeVariable<?> type) {
        Map<TypeVariable<?>, Type> typeVariableTypeMap = typeVariableAssignmentPerClass.get(declaringClass);
        if (typeVariableTypeMap == null) {
            return false;
        }

        return typeVariableTypeMap.containsKey(type);
    }

    public Type getAssignmentFor(Class<?> declaringClass, TypeVariable<?> type) {
        Map<TypeVariable<?>, Type> typeVariableTypeMap = typeVariableAssignmentPerClass.get(declaringClass);
        if (typeVariableTypeMap == null) {
            throw new IllegalStateException(String.format("We do not have information about declaringClass %s", declaringClass));
        }

        return typeVariableTypeMap.get(type);
    }
}
