package com.gmail.alfonz19.util.initialize.generator;


import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.PathNode;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractGenerator<T> {
    protected abstract T create(PathNode pathNode);

    protected List<T> create(int number, PathNode pathNode) {
        return Stream.generate(() -> create(pathNode)).limit(number).collect(Collectors.toList());
    }

    //todo hide, implement here, make final. return optional?
    public abstract CalculatedNodeData getCalculatedNodeData();
}
