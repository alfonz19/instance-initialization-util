package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.builder.PathContext;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;

public class ShortInstanceGenerator extends AbstractNumberInstanceGenerator<Short, ShortInstanceGenerator> {
    public ShortInstanceGenerator() {
        super(Short.MIN_VALUE, Short.MAX_VALUE);
    }

    @Override
    protected ShortInstanceGenerator getSelf() {
        return this;
    }

    @Override
    public Short create(PathContext pathContext) {
        return (short) RandomUtil.INSTANCE.intFromRange((int) min, (int) max);
    }
}
