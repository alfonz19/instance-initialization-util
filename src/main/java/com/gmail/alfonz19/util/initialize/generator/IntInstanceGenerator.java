package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.Config;

public class IntInstanceGenerator extends AbstractNumberInstanceGenerator<Integer, IntInstanceGenerator> {
    public IntInstanceGenerator() {
        super(Integer.class,
                MinMaxSpecification.intMinMaxSpecification(Integer.MIN_VALUE, Integer.MAX_VALUE, null)
                        .setRequestedMin(Config.UNCONFIGURED_NUMBER_MIN)
                        .setRequestedMax(Config.UNCONFIGURED_NUMBER_MAX)
        );
    }

    @Override
    protected IntInstanceGenerator getSelf() {
        return this;
    }

}
