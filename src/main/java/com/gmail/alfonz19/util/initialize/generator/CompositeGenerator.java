package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeGenerator<T> extends AbstractGenerator<T> {
    protected List<Generator<? extends T>> generators;

    protected CompositeGenerator(List<Generator<? extends T>> generators) {
        //we need to make copy of this, as it's probable, that some Arrays.asList was used on chain leading up to here.
        //also we will destroy this list in process potentially.
        this.generators = new ArrayList<>(generators);
    }

    //TODO MMUCHA: to better implement getting calculated node value, we need to be able to propagate to selected generator multiple calls, advancing only after create is called.
    protected abstract int selectGenerator();

    @Override
    public T create(PathNode pathNode) {
        if (generators.isEmpty()) {
            throw new InitializeException("No generators to use");
        }

        int generatorIndex = selectGenerator();
        Generator<? extends T> selectedGenerator = generators.get(generatorIndex);
        T result = GeneratorAccessor.create(selectedGenerator, pathNode);

        if (selectedGenerator instanceof LimitedGenerator &&
                ((LimitedGenerator<?>) selectedGenerator).canGenerateNItems() == 0) {
            generators.remove(generatorIndex);
        }

        return result;
    }
}
