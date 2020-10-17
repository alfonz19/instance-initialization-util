package com.gmail.alfonz19.util.initialize.generator;


import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.PathNode;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public abstract class AbstractStringInstanceGenerator extends AbstractGenerator<String> {
    protected final List<BiFunction<String, PathNode, String>> transformations = new LinkedList<>();

    public AbstractStringInstanceGenerator addPrefix(String prefix) {
        transformations.add((currentValue, path) -> prefix + currentValue);
        return this;
    }

    public AbstractStringInstanceGenerator addSuffix(String suffix) {
        transformations.add((currentValue, path) -> currentValue + suffix);
        return this;
    }

    //TODO MMUCHA: add updatedWithPath method.
    public AbstractStringInstanceGenerator updatedWithContext(BiFunction<String, PathNode, String> updatingFunction) {
        transformations.add(updatingFunction);
        return this;
    }

    @Override
    public String create(PathNode pathNode) {
        String result = null;
        for (BiFunction<String, PathNode, String> transformation : transformations) {
            result = transformation.apply(result, pathNode);
        }
        return result;
    }

    @Override
    public CalculatedNodeData getCalculatedNodeData() {
        return new CalculatedNodeData(String.class);
    }
}
