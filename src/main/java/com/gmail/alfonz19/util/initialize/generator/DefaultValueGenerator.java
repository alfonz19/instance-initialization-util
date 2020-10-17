package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.PathNode;
import com.gmail.alfonz19.util.initialize.util.ReflectUtil;

public class DefaultValueGenerator<T> extends AbstractGenerator<T> {

    @Override
    protected T create(PathNode pathNode) {
        //noinspection unchecked
        return (T) ReflectUtil.nullOrDefaultValue(pathNode.getCalculatedNodeData().getClassType());
    }


    @Override
    public CalculatedNodeData getCalculatedNodeData() {
        return new CalculatedNodeData(Object.class);    //TODO MMUCHA: fix.
    }
}
