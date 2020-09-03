package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.PathContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Initialize {

    public static <T> T initialize(AbstractGenerator<T> generator) {
        return initialize(generator, new PathContext());
    }

    public static <T> T initialize(AbstractGenerator<T> generator, PathContext context) {
        return generator.create(context);
    }

    public static <T> List<T> initializeList(AbstractGenerator<T> generator, int number) {
        return generator.create(number, new PathContext());
    }

    public static <T> List<T> initializeList(AbstractGenerator<T> generator, int number, PathContext context) {
        return generator.create(number, context);
    }
}
