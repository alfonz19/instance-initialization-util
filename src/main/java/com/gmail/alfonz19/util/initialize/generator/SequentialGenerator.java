package com.gmail.alfonz19.util.initialize.generator;

import java.util.List;

public class SequentialGenerator<T> extends CompositeGenerator<T> {
    public SequentialGenerator(List<AbstractGenerator<? extends T>> generators) {
        super(generators);
    }

    @Override
    protected int selectGenerator() {
        return 0;
    }
}
