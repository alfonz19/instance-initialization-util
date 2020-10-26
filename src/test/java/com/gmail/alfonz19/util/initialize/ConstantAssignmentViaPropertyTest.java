package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import com.gmail.alfonz19.util.initialize.selector.SpecificTypePropertySelector;
import lombok.Data;

import static com.gmail.alfonz19.util.initialize.ConstantAssignmentViaPropertyTest.TestInstance;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;


public class ConstantAssignmentViaPropertyTest extends AbstractTestSingleAndMultipleInstanceCreation<TestInstance> {

    public static final String CONSTANT_VALUE = "value";
    public static final SpecificTypePropertySelector<TestInstance, String> PROPERTY_SELECTOR = TestInstance::getSomeStringValue;

    @Override
    protected Generator<TestInstance> createGenerator() {
        return Generators.instance(TestInstance::new)
                .setProperty(PROPERTY_SELECTOR).toValue(CONSTANT_VALUE);
    }

    @Override
    protected void assertCreatedInstance(TestInstance rootDto) {
        assertThat(rootDto, notNullValue());
        assertThat(PROPERTY_SELECTOR.select(rootDto), notNullValue());
        assertThat(PROPERTY_SELECTOR.select(rootDto), is(CONSTANT_VALUE));
    }

    @Data
    public static class TestInstance {
        private String someStringValue;
    }
}