package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.PathNode;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;

public class LongInstanceGenerator extends AbstractNumberInstanceGenerator<Long, LongInstanceGenerator> {
    public LongInstanceGenerator() {
        super(Long.class, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    @Override
    protected LongInstanceGenerator getSelf() {
        return this;
    }

    @Override
    public Long create(PathNode pathNode) {
        return RandomUtil.INSTANCE.longFromRange(min, max);
    }
}
