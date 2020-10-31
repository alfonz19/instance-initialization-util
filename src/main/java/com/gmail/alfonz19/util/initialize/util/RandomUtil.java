package com.gmail.alfonz19.util.initialize.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
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

        return getRandom().nextLong(min, max);
    }

    public long longFromClosedRange(long min, long max) {
        if (max < min) {
            throw new IllegalArgumentException();
        }

        return min + (long)(getRandom().nextDouble() * (max - min));
    }

    public int intFromRange(int min, int max) {
        if (max <= min) {
            throw new IllegalArgumentException();
        }

        return (int) longFromRange(min, max);
    }

    public int intFromClosedRange(int min, int max) {
        if (max < min) {
            throw new IllegalArgumentException();
        }

        return (int) longFromClosedRange(min, max);
    }

    public float floatFromClosedRange(float min, float max) {
        return min + (float)(getRandom().nextDouble() * (max - min));
    }

    public double doubleFromClosedRange(double min, double max) {
        return min + (getRandom().nextDouble() * (max - min));
    }

    public BigInteger bigIntegerFromClosedRange(BigInteger min, BigInteger max) {
        BigInteger maxMinusMin = max.subtract(min);
        BigDecimal rnd = BigDecimal.valueOf(getRandom().nextDouble());
        BigDecimal rndTimesMaxMinusMin = rnd.multiply(new BigDecimal(maxMinusMin));
        BigDecimal minPlusRndTimesMaxMinusMin = rndTimesMaxMinusMin.add(new BigDecimal(min));

        return minPlusRndTimesMaxMinusMin.toBigInteger();
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
