package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.PathContext;
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
    protected T create(PathContext pathContext) {
        if (remainingNumberOfItems == 0) {
            throw new InitializeException("Cannot generate more items");
        }

        remainingNumberOfItems--;
        return generator.create(pathContext);
    }

    @Override
    protected List<T> create(int number, PathContext pathContext) {
        return Stream.generate(()-> create(pathContext))
                .limit(Math.min(number, remainingNumberOfItems))
                .collect(Collectors.toList());
    }
}
