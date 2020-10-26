package com.gmail.alfonz19.util.initialize.generator;


import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.PathNode;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractGenerator<T> implements Generator<T>{
    private boolean hasSingleCalculatedNodeData;
    private CalculatedNodeData calculatedNodeData;

    public AbstractGenerator() {
        //unless implementor says otherwise, we cannot rely on single data type to be generated, and it can be anything.
        setCalculatedNodeData(false, new CalculatedNodeData(Object.class));
    }

    public AbstractGenerator(boolean hasSingleCalculatedNodeData,
                             CalculatedNodeData calculatedNodeData) {
        setCalculatedNodeData(hasSingleCalculatedNodeData, calculatedNodeData);
    }

    protected abstract T create(PathNode pathNode);

    protected List<T> create(int number, PathNode pathNode) {
        return Stream.generate(() -> create(pathNode)).limit(number).collect(Collectors.toList());
    }

    protected final boolean hasSingleCalculatedNodeData() {
        return hasSingleCalculatedNodeData;
    }

    protected final CalculatedNodeData getCalculatedNodeData() {
        return calculatedNodeData;
    }

    protected final void setCalculatedNodeData(boolean hasSingleCalculatedNodeData, CalculatedNodeData calculatedNodeData) {
        this.hasSingleCalculatedNodeData = hasSingleCalculatedNodeData;
        this.calculatedNodeData = calculatedNodeData;
    }
}
