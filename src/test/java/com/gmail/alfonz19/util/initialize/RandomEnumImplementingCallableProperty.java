package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.testsupport.AbstractTestSingleAndMultipleInstanceCreation;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import com.gmail.alfonz19.util.initialize.selector.SpecificTypePropertySelector;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.Assert;

import static com.gmail.alfonz19.util.initialize.RandomEnumImplementingCallableProperty.TestInstance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RandomEnumImplementingCallableProperty extends AbstractTestSingleAndMultipleInstanceCreation<TestInstance> {

    @Override
    protected Generator<TestInstance> createGenerator() {
        return instance(TestInstance::new)
                .setProperty(TestInstance::getListOfEnumItemsExposedAsCallable)
                .to(Generators.list(Generators.enumeratedType(TestEnum.class).random()));
    }

    @Override
    protected void assertCreatedInstance(TestInstance testInstance) {
        assertEnumListProperty(TestInstance::getListOfEnumItemsExposedAsCallable, testInstance);
    }

    private void assertEnumListProperty(SpecificTypePropertySelector<TestInstance, List<Callable<String>>> propertySelector,
                                        TestInstance testInstance) {
        assertThat(testInstance, notNullValue());
        List<Callable<String>> list = propertySelector.select(testInstance);
        assertThat(list.size(), is(Config.UNCONFIGURED_COLLECTION_SIZE));
        list.forEach(e-> {
            assertThat(e, notNullValue());
            assertTrue(e instanceof TestEnum);

            assertTrue(Arrays.asList(TestEnum.values()).contains(e));
            try {
                assertThat(e.call(), is("hi!"));
            } catch (Exception exception) {
                Assert.fail("no exception should be raised.");
            }
        });
    }

    @Data
    public static class TestInstance {
        List<Callable<String>> listOfEnumItemsExposedAsCallable;
    }
    
    public enum TestEnum implements Callable<String> {
        A,
        B,
        C;

        public String call(){
            return "hi!";
        }
    }

}
