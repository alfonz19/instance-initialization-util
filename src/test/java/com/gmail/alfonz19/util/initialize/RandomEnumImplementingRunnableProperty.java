package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.initialize.builder.CollectionConfiguration;
import com.gmail.alfonz19.util.initialize.builder.Initialize;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import com.gmail.alfonz19.util.initialize.selector.SpecificTypePropertySelector;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

import static com.gmail.alfonz19.util.initialize.RandomEnumImplementingRunnableProperty.TestInstance;
import static com.gmail.alfonz19.util.initialize.builder.Initialize.instance;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RandomEnumImplementingRunnableProperty extends AbstractTestSingleAndMultipleInstanceCreation<TestInstance> {

    @Override
    protected Generator<TestInstance> createGenerator() {
        return instance(TestInstance::new)
                .setProperty(TestInstance::getListOfEnumItemsExposedAsRunnable)
                .to(Initialize.list(Generators.enumeratedType(TestEnum.class).random()));
    }

    @Override
    protected void assertCreatedInstance(TestInstance testInstance) {
        assertEnumListProperty(TestInstance::getListOfEnumItemsExposedAsRunnable, testInstance);
    }

    private void assertEnumListProperty(SpecificTypePropertySelector<TestInstance, List<Runnable>> propertySelector,
                                        TestInstance testInstance) {
        assertThat(testInstance, notNullValue());
        List<Runnable> list = propertySelector.select(testInstance);
        assertThat(list.size(), is(CollectionConfiguration.UNCONFIGURED_COLLECTION_SIZE));
        list.forEach(e-> {
            assertThat(e, notNullValue());
            assertTrue(e instanceof TestEnum);

            e.run();
            assertTrue(Arrays.asList(TestEnum.values()).contains(e));
        });
    }

    @Data
    public static class TestInstance {
        List<Runnable> listOfEnumItemsExposedAsRunnable;
    }
    
    @SuppressWarnings("unused")
    public enum TestEnum implements Runnable {
        A,
        B,
        C;

        public void run(){
            //not especially needed to be verified.
        }
    }

}
