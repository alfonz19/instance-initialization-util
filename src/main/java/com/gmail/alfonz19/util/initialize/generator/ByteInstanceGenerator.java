package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.builder.PathContext;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;

public class ByteInstanceGenerator extends AbstractNumberInstanceGenerator<Byte, ByteInstanceGenerator> {
    public ByteInstanceGenerator() {
        super(Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    @Override
    protected ByteInstanceGenerator getSelf() {
        return this;
    }

    @Override
    public Byte create(PathContext pathContext) {
        return (byte) RandomUtil.INSTANCE.intFromRange((int) min, (int) max);
    }
}
