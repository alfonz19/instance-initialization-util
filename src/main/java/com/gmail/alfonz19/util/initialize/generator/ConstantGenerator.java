package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.PathContext;

public class ConstantGenerator<T> extends AbstractGenerator<T> {

    private final T value;

    ConstantGenerator(T value) {
        this.value = value;
    }

    @Override
    protected T create(PathContext pathContext) {
        return value;
    }
}
