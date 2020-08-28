package com.gmail.alfonz19.util.initialize.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenericUtil {

    @SafeVarargs
    public static <T> T nvl(T first, T... others) {
        if (first != null) {
            return first;
        }

        if (others != null) {
            for (T other : others) {
                if (other != null) {
                    return other;
                }
            }
        }

        return null;
    }
}
