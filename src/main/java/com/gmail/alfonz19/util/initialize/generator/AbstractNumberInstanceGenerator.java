package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.InitializationContext;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;

@SuppressWarnings("java:S119")
abstract class AbstractNumberInstanceGenerator<T extends Number, SELF_TYPE extends AbstractNumberInstanceGenerator<T, SELF_TYPE>> extends AbstractGenerator<T>{

    private final MinMaxSpecification<T> minMaxSpecification;

    protected AbstractNumberInstanceGenerator(Class<T> classType, MinMaxSpecification<T> minMaxSpecification) {
        super(true, new CalculatedNodeData(classType));
        this.minMaxSpecification = minMaxSpecification;
    }

    protected abstract SELF_TYPE getSelf();

    public SELF_TYPE biggerThan(T min) {
        this.minMaxSpecification.setRequestedMin(min);
        return getSelf();
    }

    public SELF_TYPE smallerThan(T max) {
        this.minMaxSpecification.setRequestedMax(max);
        return getSelf();
    }

    @Override
    public final T create(InitializationContext initializationContext, PathNode pathNode) {
        return minMaxSpecification.getRandomValueAccordingToSpecification();
    }
}
