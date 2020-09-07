package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.testsupport.RepeatTest;
import com.gmail.alfonz19.testsupport.RepeatedTestRule;
import com.gmail.alfonz19.util.initialize.generator.AbstractGenerator;
import com.gmail.alfonz19.util.initialize.generator.Initialize;

import java.util.List;
import java.util.function.Consumer;

import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public abstract class AbstractTestSingleAndMultipleInstanceCreation<T> {

    @Rule
    public RepeatedTestRule repeatRule = new RepeatedTestRule();

    public static final int NUMBER_OF_INSTANCES = 5;

    @Test
    @RepeatTest(times = NUMBER_OF_INSTANCES)
    public void testCreationOfNullValuedItemsCollection() {
        T t = Initialize.initialize(createGenerator());
        assertCreatedInstance(t);
    }

    @Test
    public void testCreationOfNullValuedItemsCollectionCreatingMultipleInstances() {
        List<T> testInstances = Initialize.initializeList(createGenerator(), NUMBER_OF_INSTANCES);
        assertCollectionSizeAndEachItem(testInstances, this::assertCreatedInstance);
    }

    protected abstract AbstractGenerator<T> createGenerator();

    protected abstract void assertCreatedInstance(T t);

    protected void assertCollectionSizeAndEachItem(List<T> inputItems,
                                                       Consumer<T> collectionItemAssertion) {
        assertThat(inputItems, notNullValue());
        assertThat(inputItems.size(), is(NUMBER_OF_INSTANCES));
        for (T t : inputItems) {
            collectionItemAssertion.accept(t);
        }
    }
}
