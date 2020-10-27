package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.testsupport.AbstractTestSingleAndMultipleInstanceCreation;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import lombok.Data;

import java.util.List;

import static com.gmail.alfonz19.util.initialize.NullCollectionItemsTest.TestInstance;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class NullCollectionItemsTest extends AbstractTestSingleAndMultipleInstanceCreation<TestInstance> {

    public static final int NUMBER_OF_ITEMS = 10;

    protected Generator<TestInstance> createGenerator() {
        return Generators.instance(TestInstance::new)
                .setProperty(TestInstance::getListWithNullValuedItems).to(
                        Generators.listWithNullItems(String.class).withSize(NUMBER_OF_ITEMS)
                );
    }

    protected void assertCreatedInstance(TestInstance testInstance) {
        assertThat(testInstance, notNullValue());
        assertThat(testInstance.getListWithNullValuedItems().size(), is(NUMBER_OF_ITEMS));
        testInstance.getListWithNullValuedItems().forEach(e->assertThat(e, nullValue()));
    }

    @Data
    public static class TestInstance {
        List<String> listWithNullValuedItems;
    }
}