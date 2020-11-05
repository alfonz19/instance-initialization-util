package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.InitializationConfiguration;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;

public class ConstantGenerator<T> extends AbstractGenerator<T> {

    private final T value;

    ConstantGenerator(T value) {
        super(true, new CalculatedNodeData(value == null ? Void.class : value.getClass()));
        this.value = value;
    }

    @Override
    protected T create(InitializationConfiguration initializationConfiguration, PathNode pathNode) {
        return value;
    }
}
