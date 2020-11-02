package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.InitializationContext;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LimitedGenerator<T> extends AbstractGenerator<T> {
    private final Generator<T> generator;
    private int remainingNumberOfItems;

    public LimitedGenerator(int numberOfItems, Generator<T> generator) {
        if (numberOfItems < 1) {
            throw new IllegalArgumentException();
        }

        this.remainingNumberOfItems = numberOfItems;
        this.generator = generator;
        AbstractGenerator<T> castedGenerator = GeneratorAccessor.castGenerator(generator);
        setCalculatedNodeData(castedGenerator.hasSingleCalculatedNodeData(),
                castedGenerator.getCalculatedNodeData());
    }

    public int canGenerateNItems() {
        return remainingNumberOfItems;
    }

    @Override
    protected T create(InitializationContext initializationContext, PathNode pathNode) {
        if (remainingNumberOfItems == 0) {
            throw new InitializeException("Cannot generate more items");
        }

        remainingNumberOfItems--;
        return GeneratorAccessor.create(generator, initializationContext, pathNode);
    }

    @Override
    protected List<T> create(int number,
                             InitializationContext initializationContext,
                             PathNode pathNode) {
        return Stream.generate(()-> create(initializationContext, pathNode))
                .limit(Math.min(number, remainingNumberOfItems))
                .collect(Collectors.toList());
    }
}
