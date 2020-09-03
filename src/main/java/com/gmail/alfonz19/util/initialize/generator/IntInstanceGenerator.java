package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.PathContext;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;

public class IntInstanceGenerator extends AbstractNumberInstanceGenerator<Integer, IntInstanceGenerator> {
    public IntInstanceGenerator() {
        super(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    protected IntInstanceGenerator getSelf() {
        return this;
    }

    @Override
    public Integer create(PathContext pathContext) {
        return (int) RandomUtil.INSTANCE.longFromRange((long) min, (long) max);
    }
}
