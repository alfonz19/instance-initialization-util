package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.PathNode;

public class ConstantGenerator<T> extends AbstractGenerator<T> {

    private final T value;

    ConstantGenerator(T value) {
        this.value = value;
    }

    @Override
    protected T create(PathNode pathNode) {
        return value;
    }

    @Override
    public CalculatedNodeData getCalculatedNodeData() {
        return new CalculatedNodeData(value == null ? Void.class : value.getClass());
    }
}
