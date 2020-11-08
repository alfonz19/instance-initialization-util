package com.gmail.alfonz19.testsupport;

import com.gmail.alfonz19.util.initialize.generator.Generator;

import org.junit.Rule;
import org.junit.Test;

import static com.gmail.alfonz19.util.initialize.Initializer.create;

public abstract class AbstractTestSingleAndMultipleInstanceCreation<T> {

    @Rule
    public InitializedInstanceTestLogging initializedInstanceLogger = new InitializedInstanceTestLogging();

    @Rule
    public RepeatedTestRule repeatRule = new RepeatedTestRule();

    public static final int NUMBER_OF_INSTANCES = 5;

    @Test
    @RepeatTest(times = NUMBER_OF_INSTANCES)
    public void testCreationOfNullValuedItemsCollection() {
        T t = create(createGenerator());

        initializedInstanceLogger.logInitializedInstance(t);

        assertCreatedInstance(t);
    }

    protected abstract Generator<T> createGenerator();

    protected abstract void assertCreatedInstance(T t);
}
