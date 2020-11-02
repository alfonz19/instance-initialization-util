package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.InitializationContext;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.util.ReflectUtil;

public class DefaultValueGenerator<T> extends AbstractGenerator<T> {

    @Override
    protected T create(InitializationContext initializationContext, PathNode pathNode) {
        //noinspection unchecked
        return (T) ReflectUtil.nullOrDefaultValue(pathNode.getCalculatedNodeData().getClassType());
    }
}
