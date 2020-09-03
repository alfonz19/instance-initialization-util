package com.gmail.alfonz19.util.initialize.generator;


import com.gmail.alfonz19.util.initialize.context.PathContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractGenerator<T> {
    protected abstract T create(PathContext pathContext);

    protected List<T> create(int number, PathContext pathContext) {
        return Stream.generate(() -> create(pathContext)).limit(number).collect(Collectors.toList());
    }
}
