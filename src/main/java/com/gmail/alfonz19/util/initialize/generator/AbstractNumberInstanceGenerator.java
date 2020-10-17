package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;

@SuppressWarnings("java:S119")
abstract class AbstractNumberInstanceGenerator<T extends Number, SELF_TYPE extends AbstractNumberInstanceGenerator<T, SELF_TYPE>> extends AbstractGenerator<T>{

    private final CalculatedNodeData calculatedNodeData;
    protected T typeMin;
    protected T typeMax;
    protected T min;
    protected T max;

    protected AbstractNumberInstanceGenerator(Class<T> classType, T typeMin, T typeMax) {
        this.typeMin = typeMin;
        this.typeMax = typeMax;
        this.calculatedNodeData = new CalculatedNodeData(classType);
    }

    protected abstract SELF_TYPE getSelf();

    public SELF_TYPE biggerThan(T min) {
        this.min = min;
        return getSelf();
    }

    public SELF_TYPE smallerThan(T max) {
        this.max = max;
        return getSelf();
    }

    @Override
    public CalculatedNodeData getCalculatedNodeData() {
        return calculatedNodeData;
    }
}
