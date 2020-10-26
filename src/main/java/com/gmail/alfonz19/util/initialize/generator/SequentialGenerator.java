package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;

import java.util.List;

public class SequentialGenerator<T> extends CompositeGenerator<T> {
    public SequentialGenerator(List<Generator<? extends T>> generators) {
        super(generators);
    }

    @Override
    protected int selectGenerator() {
        return 0;
    }


    @Override
    public CalculatedNodeData getCalculatedNodeData() {
        return new CalculatedNodeData(Object.class);    //TODO MMUCHA: fix.
    }
}
