package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.testsupport.AbstractTestSingleAndMultipleInstanceCreation;
import com.gmail.alfonz19.util.initialize.exception.SpecificTypePropertySelectorDoesNotDenoteProperty;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import com.gmail.alfonz19.util.initialize.selector.SpecificTypePropertySelector;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.alfonz19.util.initialize.Initializer.create;
import static com.gmail.alfonz19.util.initialize.NullifyingPropertyTest.TestInstance;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class NullifyingPropertyTest extends AbstractTestSingleAndMultipleInstanceCreation<TestInstance> {


    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    public static final List<SpecificTypePropertySelector<TestInstance, ?>> PROPERTY_SELECTORS = Arrays.asList(
            TestInstance::getInitializedStringValue,
            TestInstance::getInitializedIntegerValue);

    @Override
    protected Generator<TestInstance> createGenerator() {
        return Generators.instance(TestInstance::new).nullifyProperties(PROPERTY_SELECTORS);
    }

    @Override
    protected void assertCreatedInstance(TestInstance rootDto) {
        assertThat(rootDto, notNullValue());
        for (SpecificTypePropertySelector<TestInstance, ?> propertySelector : PROPERTY_SELECTORS) {
            assertThat(propertySelector.select(rootDto), nullValue());
        }
    }

    @SuppressWarnings("squid:S5776")//expected exception does not have replacement in which ever JUnit 4 version, as it does not support comparing exception message.
    @Test
    public void testNullifyingFinalProperty() {
        SpecificTypePropertySelector<TestInstance, ?> propertySelector = TestInstance::getInitializedFinalStringValue;

        expectedException.expect(SpecificTypePropertySelectorDoesNotDenoteProperty.class);
        expectedException.expectMessage("SpecificTypePropertySelector in class 'com.gmail.alfonz19.util.initialize.NullifyingPropertyTest$TestInstance' does not select property, method 'getInitializedFinalStringValue' s probably just a getter.");
        TestInstance rootDto = create(Generators.instance(TestInstance::new)
                .nullifyProperty(propertySelector));

        assertThat(rootDto, notNullValue());
        assertThat(propertySelector.select(rootDto), nullValue());
    }

    @Data
    public static class TestInstance {
        private String initializedStringValue = "initialized";
        private Integer initializedIntegerValue = 123;

        @SuppressWarnings("squid:S1170")
        private final String initializedFinalStringValue = "initialized";
    }

}