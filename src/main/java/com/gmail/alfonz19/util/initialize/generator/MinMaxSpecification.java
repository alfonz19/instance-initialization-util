package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;
import lombok.Getter;

import java.math.BigInteger;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import static com.gmail.alfonz19.util.initialize.util.GenericUtil.nvl;

public class MinMaxSpecification<T extends Number> {
    @Getter
    private final T min;
    @Getter
    private final T max;
    @Getter
    private final T unconfiguredValue;
    @Getter
    private T requestedMin;
    @Getter
    private T requestedMax;
    @Getter
    private T requestedValue;

    private final BiPredicate<T, T> lt;
    private final BiPredicate<T, T> gt;
    private final BiFunction<T, T, T> rnd;

    private MinMaxSpecification(T min, T max, T unconfiguredValue, BiPredicate<T,T> lt, BiPredicate<T,T> gt, BiFunction<T, T, T> rnd) {
        this.min = Objects.requireNonNull(min);
        this.max = Objects.requireNonNull(max);
        this.unconfiguredValue = unconfiguredValue;
        this.lt = Objects.requireNonNull(lt);
        this.gt = Objects.requireNonNull(gt);
        this.rnd = rnd;
    }

    public static MinMaxSpecification<Integer> intMinMaxSpecification(Integer min, Integer max, Integer unconfiguredValue) {
        return new MinMaxSpecification<>(min,
                max,
                unconfiguredValue,
                (a, b) -> a < b,
                (a, b) -> a > b,
                RandomUtil.INSTANCE::intFromClosedRange);
    }

    public static MinMaxSpecification<Short> shortMinMaxSpecification(Short min, Short max, Short unconfiguredValue) {
        return new MinMaxSpecification<>(min,
                max,
                unconfiguredValue,
                (a, b) -> a < b,
                (a, b) -> a > b,
                (a, b) -> (short) RandomUtil.INSTANCE.intFromClosedRange((int) a, (int) b));
    }

    public static MinMaxSpecification<Long> longMinMaxSpecification(Long min, Long max, Long unconfiguredValue) {
        return new MinMaxSpecification<>(min,
                max,
                unconfiguredValue,
                (a, b) -> a < b,
                (a, b) -> a > b,
                RandomUtil.INSTANCE::longFromClosedRange);
    }

    public static MinMaxSpecification<BigInteger> longMinMaxSpecification(BigInteger min, BigInteger max, BigInteger unconfiguredValue) {
        return new MinMaxSpecification<>(min,
                max,
                unconfiguredValue,
                (a, b) -> a.compareTo(b) < 0,
                (a, b) -> a.compareTo(b) > 0,
                RandomUtil.INSTANCE::bigIntegerFromClosedRange);
    }

    public static MinMaxSpecification<Byte> byteMinMaxSpecification(Byte min, Byte max, Byte unconfiguredValue) {
        return new MinMaxSpecification<>(min,
                max,
                unconfiguredValue,
                (a, b) -> a < b,
                (a, b) -> a > b,
                (a, b) -> (byte) RandomUtil.INSTANCE.intFromClosedRange(a, b));
    }

    public static MinMaxSpecification<Float> floatMinMaxSpecification(Float min, Float max, Float unconfiguredValue) {
        return new MinMaxSpecification<>(min,
                max,
                unconfiguredValue,
                (a, b) -> a < b,
                (a, b) -> a > b,
                RandomUtil.INSTANCE::floatFromClosedRange);
    }

    public static MinMaxSpecification<Double> doubleMinMaxSpecification(Double min, Double max, Double unconfiguredValue) {
        return new MinMaxSpecification<>(min,
                max,
                unconfiguredValue,
                (a, b) -> a < b,
                (a, b) -> a > b,
                RandomUtil.INSTANCE::doubleFromClosedRange);
    }


    public MinMaxSpecification<T> setRequestedMin(T requestedMin) {
        if (lt.test(requestedMin, min)) {
            throw new InitializeException("Requested min value cannot be smaller than " + min);
        }

        this.requestedMin = requestedMin;
        validateSizeSpecification();
        return this;
    }

    public MinMaxSpecification<T> setRequestedMax(T requestedMax) {
        if (gt.test(requestedMax, max)) {
            throw new InitializeException("Requested max valuesize cannot be bigger than " + max);
        }

        this.requestedMax = requestedMax;
        validateSizeSpecification();
        return this;
    }

    public MinMaxSpecification<T> setRequestedValue(T requestedValue) {
        if (lt.test(requestedValue, min) || gt.test(requestedValue, max)) {
            throw new InitializeException(String.format("Size has to be in range <%s, %s>", min, max));
        }

        this.requestedValue = requestedValue;
        validateSizeSpecification();
        return this;
    }

    private void validateSizeSpecification() {
        if (requestedValue != null && (requestedMin != null || requestedMax != null)) {
            throw new InitializeException("Specified both min-max size bounds and exact size size.");
        }
    }

    public T getRandomValueAccordingToSpecification() {
        if (this.requestedValue != null) {
            return this.requestedValue;
        }

        if (unconfiguredValue != null && requestedMin == null && requestedMax == null) {
            return unconfiguredValue;
        }

        return rnd.apply(nvl(requestedMin, min), nvl(requestedMax, max));
    }
}
