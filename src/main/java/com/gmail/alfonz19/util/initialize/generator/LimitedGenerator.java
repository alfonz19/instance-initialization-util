package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.InitializationConfiguration;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;

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
    protected T create(InitializationConfiguration initializationConfiguration, PathNode pathNode) {
        if (remainingNumberOfItems == 0) {
            throw new InitializeException("Cannot generate more items");
        }

        remainingNumberOfItems--;
        return GeneratorAccessor.create(generator, initializationConfiguration, pathNode);
    }
}
