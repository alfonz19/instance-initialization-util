package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;

import java.util.List;

public class RoundRobinGenerator<T> extends CompositeGenerator<T> {
    private int indexOfGeneratorToUse;

    public RoundRobinGenerator(List<AbstractGenerator<? extends T>> generators) {
        super(generators);
        //pretend, that last generator was used last time.
        indexOfGeneratorToUse = generators.size() - 1;
    }

    @Override
    protected int selectGenerator() {
        indexOfGeneratorToUse = (indexOfGeneratorToUse + 1) % generators.size();
        return indexOfGeneratorToUse;
    }

    @Override
    public CalculatedNodeData getCalculatedNodeData() {
        return new CalculatedNodeData(Object.class);    //TODO MMUCHA: fix.
    }
}
