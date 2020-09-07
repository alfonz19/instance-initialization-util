package com.gmail.alfonz19.util.initialize.generator;

public class ConstantGenerator<T> extends AbstractGenerator<T> {

    private final T value;

    ConstantGenerator(T value) {
        this.value = value;
    }

    @Override
    protected T create() {
        return value;
    }
}
