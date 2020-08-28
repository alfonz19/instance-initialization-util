package com.gmail.alfonz19.util.initialize.generator;

@SuppressWarnings("java:S119")
abstract class AbstractNumberInstanceGenerator<T extends Number, SELF_TYPE extends AbstractNumberInstanceGenerator<T, SELF_TYPE>> implements Generator<T>{

    protected T typeMin;
    protected T typeMax;
    protected T min;
    protected T max;

    protected AbstractNumberInstanceGenerator(T typeMin, T typeMax) {
        this.typeMin = typeMin;
        this.typeMax = typeMax;
    }

    protected abstract SELF_TYPE getSelf();

    public SELF_TYPE biggerThan(T min) {
        this.typeMin = min;
        return getSelf();
    }

    public SELF_TYPE smallerThan(T max) {
        this.max = max;
        return getSelf();
    }

}
