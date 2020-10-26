package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.initialize.generator.Generator;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static com.gmail.alfonz19.util.initialize.SettingRandomValueViaPropertyGeneratingValueBasedOnClassTypeTest.TestInstance;

public class SettingRandomValueViaPropertyGeneratingValueBasedOnClassTypeTest extends AbstractTestSingleAndMultipleInstanceCreation<TestInstance>{

    @Override
    protected Generator<TestInstance> createGenerator() {
        return instance(TestInstance::new).setUnsetPropertiesRandomlyUsingGuessedType();
    }

    @Override
    protected void assertCreatedInstance(TestInstance testInstance) {
        assertThat(testInstance.getN1(), nullValue());
        assertThat(testInstance.getSomeUUIDValue(), notNullValue());
    }

    @Data
    public static class TestInstance {
        private UUID someUUIDValue;
        private Nested1 n1;
    }

    @Data
    public static class Nested1 {
        private String someStringValue;
        private Nested2 n2;

    }

    @Data
    public static class Nested2 {
        private int i;
        private Integer ii;
        private Date someDate;
    }
}