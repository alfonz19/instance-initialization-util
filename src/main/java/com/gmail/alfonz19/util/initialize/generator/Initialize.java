package com.gmail.alfonz19.util.initialize.generator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Initialize {

    public static <T> T initialize(AbstractGenerator<T> generator) {
        return generator.create();
    }

    public static <T> List<T> initializeList(AbstractGenerator<T> generator, int number) {
        return generator.create(number);
    }
}
