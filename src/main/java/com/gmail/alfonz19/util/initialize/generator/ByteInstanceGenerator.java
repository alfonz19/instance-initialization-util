package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.Config;

public class ByteInstanceGenerator extends AbstractNumberInstanceGenerator<Byte, ByteInstanceGenerator> {
    public ByteInstanceGenerator() {
        super(Byte.class,
                MinMaxSpecification.byteMinMaxSpecification(Byte.MIN_VALUE, Byte.MAX_VALUE, null)
                        .setRequestedMin(capByte(Config.UNCONFIGURED_NUMBER_MIN))
                        .setRequestedMax(capByte(Config.UNCONFIGURED_NUMBER_MAX)));
    }

    private static byte capByte(int value) {
        return value > Byte.MAX_VALUE ? Byte.MAX_VALUE : (byte) Config.UNCONFIGURED_NUMBER_MIN;
    }

    @Override
    protected ByteInstanceGenerator getSelf() {
        return this;
    }
}
