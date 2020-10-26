package com.gmail.alfonz19.util.initialize.generator;

import java.util.List;

/**
 * Probably only useful with combination of {@link LimitedGenerator}. Will always use first generator from N generators.
 */
public class SequentialGenerator<T> extends CompositeGenerator<T> {
    public SequentialGenerator(List<Generator<? extends T>> generators) {
        super(generators);
    }

    @Override
    protected int selectGenerator() {
        return 0;
    }
}
