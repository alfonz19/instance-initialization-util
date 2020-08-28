package com.gmail.alfonz19.util.initialize.generator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Generators {

    public static StringInstanceGenerator randomString() {
        return new StringInstanceGenerator();
    }

    public static IntInstanceGenerator randomInt() {
        return new IntInstanceGenerator();
    }

    public static IntInstanceGenerator randomIntFromRange(int min, int max) {
        return new IntInstanceGenerator().biggerThan(min).smallerThan(max);
    }

    public static LongInstanceGenerator randomLong() {
        return new LongInstanceGenerator();
    }

    public static LongInstanceGenerator randomLongFromRange(long min, long max) {
        return new LongInstanceGenerator().biggerThan(min).smallerThan(max);
    }

    public static ShortInstanceGenerator randomShort() {
        return new ShortInstanceGenerator();
    }

    public static ShortInstanceGenerator randomShortFromRange(short min, short max) {
        return new ShortInstanceGenerator().biggerThan(min).smallerThan(max);
    }

    public static ByteInstanceGenerator randomByte(byte min, byte max) {
        return new ByteInstanceGenerator();
    }

    public static ByteInstanceGenerator randomByteFromRange(byte min, byte max) {
        return new ByteInstanceGenerator().biggerThan(min).smallerThan(max);
    }

    public static <T> EnumInstanceGenerator<T, T> enumeratedType(Class<T> classType) {
        return new EnumInstanceGenerator<>(classType, classType);
    }

    @SafeVarargs
    public static <T> Generator<T> roundRobinGenerator(Generator<T> ... generators) {
        return new RoundRobinGenerator<>(Arrays.asList(generators));
    }

    @SafeVarargs
    public static <T> Generator<T> sequentialGenerator(Generator<? extends T> ... generators) {
        return new SequentialGenerator<>(Arrays.asList(generators));
    }

    @SafeVarargs
    public static <T> Generator<T> randomlySelectedGenerator(Generator<T> ... generators) {
        return new RandomlySelectedGenerator<>(Arrays.asList(generators));
    }

    public static <T> Generator<T> limitedGenerator(int numberOfItems, Generator<T> generator) {
        return new LimitedGenerator<>(numberOfItems, generator);
    }

    public static <T> Generator<T> constantGenerator(T value) {
        return () -> value;
    }

    public static <T> Generator<T> nullGenerator() {
        return () -> null;
    }

}
