package com.gmail.alfonz19.util.initialize.generator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Generator<T> {
    T create();

    default List<T> create(int number) {
        return Stream.generate(this::create).limit(number).collect(Collectors.toList());
    }
}
