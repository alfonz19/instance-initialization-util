package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.builder.PathContext;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;

public class LongInstanceGenerator extends AbstractNumberInstanceGenerator<Long, LongInstanceGenerator> {
    public LongInstanceGenerator() {
        super(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    @Override
    protected LongInstanceGenerator getSelf() {
        return this;
    }

    @Override
    public Long create(PathContext pathContext) {
        return RandomUtil.INSTANCE.longFromRange(min, max);
    }
}
