package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.initialize.builder.CollectionConfiguration;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.selector.SpecificTypePropertySelector;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

import static com.gmail.alfonz19.util.initialize.RandomEnumPropertyTest.TestInstance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.enumeratedType;
import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.list;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RandomEnumPropertyTest extends AbstractTestSingleAndMultipleInstanceCreation<TestInstance> {

    public static final SpecificTypePropertySelector<TestInstance, List<TestEnum>>
            PROPERTY_SELECTOR = TestInstance::getListOfEnumItems;

    @Override
    protected Generator<TestInstance> createGenerator() {
        return instance(TestInstance::new)
                .setProperty(PROPERTY_SELECTOR).to(list(enumeratedType(TestEnum.class).random()));
    }

    @Override
    protected void assertCreatedInstance(TestInstance testInstance) {
        assertThat(testInstance, notNullValue());
        List<TestEnum> list = PROPERTY_SELECTOR.select(testInstance);
        assertThat(list.size(), is(CollectionConfiguration.UNCONFIGURED_COLLECTION_SIZE));
        list.forEach(e-> {
            assertThat(e, notNullValue());
            assertTrue(Arrays.asList(TestEnum.values()).contains(e));
        });
    }

    @Data
    public static class TestInstance {
        List<TestEnum> listOfEnumItems;
        List<Runnable> listOfEnumItemsExposedAsRunnable;
        List<String> unrelated;
    }

    public enum TestEnum {
        A,
        B,
        C
    }

}
