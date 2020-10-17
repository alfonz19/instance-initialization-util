package com.gmail.alfonz19.util.initialize.util;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TypeReferenceUtil {
    public static <T> Class<T> getRawTypeClassType(TypeReference<T> typeReference) {
        Type typeOfTypeReference = typeReference.getType();
        return ReflectUtil.getRawType(typeOfTypeReference);
    }

}
