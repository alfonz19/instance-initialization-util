package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.testsupport.AbstractTestSingleAndMultipleInstanceCreation;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import lombok.Data;

import static com.gmail.alfonz19.util.initialize.SimpleCreationTest.TestInstance;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SimpleCreationTest extends AbstractTestSingleAndMultipleInstanceCreation<TestInstance> {

    @Override
    protected Generator<TestInstance> createGenerator() {
        return Generators.instance(TestInstance::new);
    }

    @Override
    protected void assertCreatedInstance(TestInstance t) {
        assertThat(t, notNullValue());
    }

    @Data
    public static class TestInstance {
    }
}