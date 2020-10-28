package com.gmail.alfonz19.util.initialize.util;

import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClassDataCache {
    private final static Map<Class<?>, ClassDetails> classDetailsMap = new HashMap<>();

    public static boolean canBeInstantiatedUsingNoArgConstructor(Class<?> classType) {
        ClassDetails detail = classDetailsMap.computeIfAbsent(classType, ct -> new ClassDetails());

        if (detail.canBeInstantiatedUsingNoArgConstructor != null) {
            return detail.canBeInstantiatedUsingNoArgConstructor;
        } else {
            initNoArgConstructor(classType, detail);

            return detail.canBeInstantiatedUsingNoArgConstructor;
        }
    }

    public static void initNoArgConstructor(Class<?> classType, ClassDetails detail) {
        boolean isAbstract = Modifier.isAbstract(classType.getModifiers());
        if (isAbstract) {
            detail.canBeInstantiatedUsingNoArgConstructor = false;
        } else {
            Constructor<?> constructor = findNoArgConstructorAndReturnNullIfOneDoesNotExist(classType);
            detail.noArgConstructor = constructor;
            detail.canBeInstantiatedUsingNoArgConstructor = constructor != null;
        }
    }

    public static <T> Constructor<T> getNoArgConstructor(Class<T> classType) {
        ClassDetails detail = classDetailsMap.computeIfAbsent(classType, ct -> new ClassDetails());

        if (detail.canBeInstantiatedUsingNoArgConstructor != null) {
            if (detail.canBeInstantiatedUsingNoArgConstructor) {
                //noinspection unchecked
                return (Constructor<T>) detail.noArgConstructor;
            } else {
                throw new InitializeException("Weird state.");
            }
        } else {
            initNoArgConstructor(classType, detail);

            //noinspection unchecked
            return (Constructor<T>) detail.noArgConstructor;
        }
    }

    private static <T> Constructor<T> findNoArgConstructorAndReturnNullIfOneDoesNotExist(Class<T> classType) {
        try {
            return classType.getConstructor();
        } catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
    }

    private static class ClassDetails {
        Boolean canBeInstantiatedUsingNoArgConstructor = null;
        Constructor<?> noArgConstructor = null;
    }
}
