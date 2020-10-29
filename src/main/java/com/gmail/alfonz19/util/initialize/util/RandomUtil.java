package com.gmail.alfonz19.util.initialize.util;

import jdk.internal.dynalink.support.AutoDiscovery;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@SuppressWarnings("squid:S125")
public enum RandomUtil {
    INSTANCE;

    public long longFromRange(long min, long max) {
        if (max <= min) {
            throw new IllegalArgumentException();
        }

//        return getRandom().nextLong(max - min) + min;
        return getRandom().nextLong(min, max);
    }

    public long longFromClosedRange(long min, long max) {
        if (max < min) {
            throw new IllegalArgumentException();
        }

//        return getRandom().nextLong(max - min + 1) + min;

        //overflow detection
        long maxToUse = max + 1 < 0 ? max : max + 1;
        return getRandom().nextLong(min, maxToUse);
    }

    public int intFromRange(int min, int max) {
        if (max <= min) {
            throw new IllegalArgumentException();
        }

//        return getRandom().nextInt((int)((long) max - (long) min)) + min;
        return (int) longFromRange(min, max);
    }

    public int intFromClosedRange(int min, int max) {
        if (max < min) {
            throw new IllegalArgumentException();
        }

//        return getRandom().nextInt(max - min + 1) + min;
        return (int) longFromClosedRange(min, max);
    }

    public float floatFromClosedRange(float min, float max) {
        //TODO MMUCHA: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public double doubleFromClosedRange(double min, double max) {
        //TODO MMUCHA: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public BigInteger bigIntegerFromClosedRange(BigInteger min, BigInteger max) {
        //TODO MMUCHA: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public <T> T randomFromList(List<T> list) {
        return list.get(intFromRange(0, list.size()));
    }

    public <T> T randomEnum(Class<T> enumClass) {
        T[] enumConstants = enumClass.getEnumConstants();
        if (enumConstants == null) {
            log.error("Coding error, {} is not an enum class", enumClass);
            return null;
        }
        List<T> asList = Arrays.asList(enumConstants);
        return randomFromList(asList);
    }

    public String randomStringFromDecimalNumbers(int size) {
        int remainingSize = size;
        StringBuilder stringBuilder = new StringBuilder();
        while (remainingSize > 0) {
            long randomLong = getRandom().nextLong();
            if (randomLong < 0) {
                randomLong *= -1;
            }
            String str = Long.toString(randomLong);
            int strLength = str.length();
            stringBuilder.append(str, 0, remainingSize);
            remainingSize -= strLength;
        }
        return stringBuilder.toString();

    }

    public ThreadLocalRandom getRandom() {
        //we're using this instead of base Random, because namely this one offers also bounded long random method.
        return ThreadLocalRandom.current();
    }
}
