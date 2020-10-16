package com.gmail.alfonz19.util.initialize.util;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TypeReferenceUtil {
    public static <T> Class<T> getRawTypeClassType(TypeReference<T> typeReference) {
        ParameterizedType type = (ParameterizedType) typeReference.getType();
        Type rawType = type.getRawType();
        if (!(rawType instanceof Class)) {
            throw new IllegalArgumentException("Cannot create supplier, raw type isn't class.");
        }
        //noinspection unchecked
        return (Class<T>) rawType;
    }
}
