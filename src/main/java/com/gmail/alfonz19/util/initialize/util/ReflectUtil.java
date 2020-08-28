package com.gmail.alfonz19.util.initialize.util;

import com.gmail.alfonz19.util.initialize.exception.InitializeException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.function.Supplier;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public enum ReflectUtil {
    INSTANCE;

    public static <T> Supplier<T> supplierFromClass(Class<T> instance) {
        return () -> {
            try {
                return instance.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new InitializeException(e);
            }
        };
    }

    /**
     * @return Array of {@code arrayType} containing all given {@code items}.
     */
    public static <ITEM_TYPE> ITEM_TYPE[] createArray(Class<ITEM_TYPE> arrayType, Collection<? extends ITEM_TYPE> items) {
        Object array = Array.newInstance(arrayType, items.size());
        //noinspection unchecked    //there is not way how to do this in type-safe way.
        ITEM_TYPE[] castedArray = (ITEM_TYPE[]) array;
        return items.toArray(castedArray);
    }
}
