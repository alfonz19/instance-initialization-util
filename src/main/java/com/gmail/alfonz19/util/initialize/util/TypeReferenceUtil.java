package com.gmail.alfonz19.util.initialize.util;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TypeReferenceUtil {
    public static <T> Class<T> getRawTypeClassType(TypeReference<T> typeReference) {
        Type typeOfTypeReference = typeReference.getType();
        if (typeOfTypeReference instanceof Class) {
            //noinspection unchecked
            return (Class<T>) typeOfTypeReference;
        }

        ParameterizedType type = (ParameterizedType) typeOfTypeReference;
        Type rawType = type.getRawType();
        if (!(rawType instanceof Class)) {
            throw new IllegalArgumentException("Cannot create supplier, raw type isn't class.");
        }
        //noinspection unchecked
        return (Class<T>) rawType;
    }

    public static <SOURCE_INSTANCE> Type[] getActualTypeArguments(TypeReference<SOURCE_INSTANCE> typeReference) {
        Type typeOfTypeReference = typeReference.getType();
        if (typeOfTypeReference instanceof Class) {
            return new Type[0];
        }

        return ((ParameterizedType) typeOfTypeReference).getActualTypeArguments();
    }
}
