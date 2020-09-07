package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.initialize.generator.AbstractGenerator;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import com.gmail.alfonz19.util.initialize.selector.SpecificTypePropertySelector;
import lombok.Data;

import org.hamcrest.Matchers;

import static com.gmail.alfonz19.util.initialize.SettingRandomValueViaPropertyTest.TestInstance;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SettingRandomValueViaPropertyTest extends AbstractTestSingleAndMultipleInstanceCreation<TestInstance>{

    public static final SpecificTypePropertySelector<TestInstance, String> PROPERTY_SELECTOR = TestInstance::getSomeStringValue;

    @Override
    protected AbstractGenerator<TestInstance> createGenerator() {
        return Generators.instance(TestInstance::new)
                .setProperty(PROPERTY_SELECTOR).to(Generators.randomString().withSize(10).addPrefix("abc"));
    }

    @Override
    protected void assertCreatedInstance(TestInstance rootDto) {
        assertThat(rootDto, notNullValue());
        assertThat(PROPERTY_SELECTOR.select(rootDto), notNullValue());
        assertThat(PROPERTY_SELECTOR.select(rootDto), Matchers.matchesPattern("^abc\\d{10}$"));
    }

    @Data
    public static class TestInstance {
        private String someStringValue;
    }
}