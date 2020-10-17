package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;

import java.util.List;

public class RandomlySelectedGenerator<T> extends CompositeGenerator<T> {
    public RandomlySelectedGenerator(List<AbstractGenerator<? extends T>> generators) {
        super(generators);
    }

    @Override
    protected int selectGenerator() {
        return RandomUtil.INSTANCE.intFromRange(0, generators.size());
    }


    @Override
    public CalculatedNodeData getCalculatedNodeData() {
        return new CalculatedNodeData(Object.class);    //TODO MMUCHA: fix.
    }
}
