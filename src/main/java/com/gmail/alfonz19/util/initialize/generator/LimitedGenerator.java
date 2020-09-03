package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.builder.PathContext;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LimitedGenerator<T> implements Generator<T> {
    private final Generator<T> generator;
    private int remainingNumberOfItems;

    public LimitedGenerator(int numberOfItems, Generator<T> generator) {
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
    public T create(PathContext pathContext) {
        if (remainingNumberOfItems == 0) {
            throw new InitializeException("Cannot generate more items");
        }

        remainingNumberOfItems--;
        return generator.create(pathContext);
    }

    @Override
    public List<T> create(int number) {
        return Stream.generate(this::create)
                .limit(Math.min(number, remainingNumberOfItems))
                .collect(Collectors.toList());
    }

    @Override
    public List<T> create(int number, PathContext pathContext) {
        return Stream.generate(()-> create(pathContext))
                .limit(Math.min(number, remainingNumberOfItems))
                .collect(Collectors.toList());
    }
}
