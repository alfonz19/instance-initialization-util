package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.example.to.AssociatedClass;
import com.gmail.alfonz19.util.example.to.RootDto;
import com.gmail.alfonz19.util.example.to.TestingInterface;
import com.gmail.alfonz19.util.initialize.generator.AbstractGenerator;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import com.gmail.alfonz19.util.initialize.selector.SpecificTypePropertySelector;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;

import static com.gmail.alfonz19.util.initialize.SettingAllPropertiesOfGivenTypeToRandomValueViaPropertyTest.TestInstance;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SettingAllPropertiesOfGivenTypeToRandomValueViaPropertyTest extends AbstractTestSingleAndMultipleInstanceCreation<TestInstance>{

    @Override
    protected AbstractGenerator<TestInstance> createGenerator() {
        return Generators.instance(TestInstance::new)
                .setAllPropertiesHavingType(String.class).to(Generators.randomString().withSize(10).addPrefix("abc"));
    }

    @Override
    protected void assertCreatedInstance(TestInstance rootDto) {
        assertThat(rootDto, notNullValue());

        List<SpecificTypePropertySelector<TestInstance, String>> propertySelectors = Arrays.asList(
                TestInstance::getInitializedStringValue,
                TestInstance::getSomeStringValue);

        propertySelectors.forEach(e->assertSimpleSetterToRandomValue(e, rootDto));

        //each initialized string is different.
        assertThat(propertySelectors.stream().map(e->e.select(rootDto)).collect(Collectors.toSet()).size(),
                is(propertySelectors.size()));
    }

    private void assertSimpleSetterToRandomValue(SpecificTypePropertySelector<TestInstance, String> propertySelector,
                                                 TestInstance rootDto) {
        assertThat(propertySelector.select(rootDto), notNullValue());
        assertThat(propertySelector.select(rootDto), Matchers.matchesPattern("^abc\\d{10}$"));
    }

    @Data
    public static class TestInstance {
        private int someIntegerProperty;
        private int someIntegerProperty2;
        private String someStringValue;
        private String initializedStringValue = "initialized";
        private Integer initializedIntegerValue = 123;

        @SuppressWarnings("squid:S1170")
        private final String initializedFinalStringValue = "initialized";
        private List<AssociatedClass> listOfAssociatedClasses;
        private List<List<AssociatedClass>> listOfListsOfAssociatedClasses;
        private List<TestingInterface> listInterfaces;
        private RootDto anotherRootDtoToInit;
        private Map<List<AssociatedClass>, Set<TestingInterface>> complicatedMap;
    }

}