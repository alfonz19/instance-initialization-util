package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.PathNode;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LimitedGenerator<T> extends AbstractGenerator<T> {
    private final AbstractGenerator<T> generator;
    private int remainingNumberOfItems;

    public LimitedGenerator(int numberOfItems, AbstractGenerator<T> generator) {
        if (numberOfItems < 1) {
            throw new IllegalArgumentException();
        }

        this.remainingNumberOfItems = numberOfItems;
        this.generator = generator;
    }

    public int canGenerateNItems() {
        return remainingNumberOfItems;
    }

    @Override
    protected T create(PathNode pathNode) {
        if (remainingNumberOfItems == 0) {
            throw new InitializeException("Cannot generate more items");
        }

        remainingNumberOfItems--;
        return generator.create(pathNode);
    }

    @Override
    protected List<T> create(int number, PathNode pathNode) {
        return Stream.generate(()-> create(pathNode))
                .limit(Math.min(number, remainingNumberOfItems))
                .collect(Collectors.toList());
    }

    @Override
    public CalculatedNodeData getCalculatedNodeData() {
        return generator.getCalculatedNodeData();
    }
}
