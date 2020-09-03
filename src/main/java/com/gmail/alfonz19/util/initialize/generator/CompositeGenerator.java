package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.PathContext;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeGenerator<T> extends AbstractGenerator<T> {
    protected List<AbstractGenerator<? extends T>> generators;

    protected CompositeGenerator(List<AbstractGenerator<? extends T>> generators) {
        //we need to make copy of this, as it's probable, that some Arrays.asList was used on chain leading up to here.
        //also we will destroy this list in process potentially.
        this.generators = new ArrayList<>(generators);
    }

    protected abstract int selectGenerator();

    @Override
    public T create(PathContext pathContext) {
        if (generators.isEmpty()) {
            throw new InitializeException("No generators to use");
        }

        int generatorIndex = selectGenerator();
        AbstractGenerator<? extends T> selectedGenerator = generators.get(generatorIndex);
        T result = selectedGenerator.create(pathContext);

        if (selectedGenerator instanceof LimitedGenerator &&
                ((LimitedGenerator<?>) selectedGenerator).canGenerateNItems() == 0) {
            generators.remove(generatorIndex);
        }

        return result;
    }
}
