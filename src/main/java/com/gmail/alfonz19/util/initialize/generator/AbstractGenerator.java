package com.gmail.alfonz19.util.initialize.generator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractGenerator<T> {
    protected abstract T create();

    protected List<T> create(int number) {
        return Stream.generate(this::create).limit(number).collect(Collectors.toList());
    }
}
