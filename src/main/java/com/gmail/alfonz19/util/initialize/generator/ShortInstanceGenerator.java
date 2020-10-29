package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.Config;

public class ShortInstanceGenerator extends AbstractNumberInstanceGenerator<Short, ShortInstanceGenerator> {
    public ShortInstanceGenerator() {
        super(Short.class,
                MinMaxSpecification.shortMinMaxSpecification(Short.MIN_VALUE, Short.MAX_VALUE, null)
                        .setRequestedMin(capShort(Config.UNCONFIGURED_NUMBER_MIN))
                        .setRequestedMax(capShort(Config.UNCONFIGURED_NUMBER_MAX)));
    }

    private static short capShort(int value) {
        return value > Short.MAX_VALUE ? Short.MAX_VALUE : (short) Config.UNCONFIGURED_NUMBER_MIN;
    }

    @Override
    protected ShortInstanceGenerator getSelf() {
        return this;
    }
}
