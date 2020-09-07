package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SettingRandomValueViaPropertyGeneratingValueBasedOnClassTypeTest /*extends AbstractTestSingleAndMultipleInstanceCreation<TestInstance>*/{


//    @Override
//    protected Generator<TestInstance> createGenerator() {
//        return Initialize.instance(TestInstance::new).setUnsetPropertiesRandomly()
//                .setProperty(PROPERTY_SELECTOR).to(Generators.randomString().withSize(10).addPrefix("abc"));
//    }

//    @Override
    protected void assertCreatedInstance(TestInstance rootDto) {
//        assertThat(rootDto, notNullValue());
//        assertThat(PROPERTY_SELECTOR.select(rootDto), notNullValue());
//        assertThat(PROPERTY_SELECTOR.select(rootDto), Matchers.matchesPattern("^abc\\d{10}$"));
    }

    @Test
    public void name() {
        Generator<TestInstance> generator =
                Generators.instance(TestInstance::new).setUnsetPropertiesRandomlyUsingGuessedType();

        TestInstance testInstance = generator.create();
        System.out.println(testInstance);
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