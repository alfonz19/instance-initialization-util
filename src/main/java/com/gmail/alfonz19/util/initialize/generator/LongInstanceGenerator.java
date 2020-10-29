package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.Config;

public class LongInstanceGenerator extends AbstractNumberInstanceGenerator<Long, LongInstanceGenerator> {
    public LongInstanceGenerator() {
        super(Long.class,
                MinMaxSpecification.longMinMaxSpecification(Long.MIN_VALUE, Long.MAX_VALUE, null)
                        .setRequestedMin((long) Config.UNCONFIGURED_NUMBER_MIN)
                        .setRequestedMax((long) Config.UNCONFIGURED_NUMBER_MAX));
    }

    @Override
    protected LongInstanceGenerator getSelf() {
        return this;
    }
}
