package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.example.to.AssociatedClass;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import com.gmail.alfonz19.util.initialize.selector.SpecificTypePropertySelector;
import lombok.Data;

import java.util.List;

import static com.gmail.alfonz19.util.initialize.SettingRandomValueViaPropertyHavingListTypeTest.RootDto;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

public class SettingRandomValueViaPropertyHavingListTypeTest extends AbstractTestSingleAndMultipleInstanceCreation<RootDto>{

    public static final SpecificTypePropertySelector<RootDto, List<AssociatedClass>> PROPERTY_SELECTOR =
            RootDto::getListOfAssociatedClasses;
    public static final int MIN_SIZE = 2;
    public static final int MAX_SIZE = 3;

    @Override
    protected Generator<RootDto> createGenerator() {
        return Generators.instance(RootDto::new)
                .setProperty(PROPERTY_SELECTOR)
                .to(Generators.list(Generators.instance(AssociatedClass.class)
                        .setAllPropertiesHavingType(Integer.TYPE)
                        .toValue(1))

                        .withMinSize(MIN_SIZE)
                        .withMaxSize(MAX_SIZE));
    }

    @Override
    protected void assertCreatedInstance(RootDto rootDto) {
        assertThat(rootDto, notNullValue());
        List<AssociatedClass> values = PROPERTY_SELECTOR.select(rootDto);
        int size = values.size();
        assertThat(size, allOf(greaterThanOrEqualTo(MIN_SIZE), lessThanOrEqualTo(MAX_SIZE)));
        for (AssociatedClass value : values) {
            assertThat(value.getA(), is(1));
            assertThat(value.getB(), is(1));
        }
    }

    @Data
    public static class RootDto {
        private List<AssociatedClass> listOfAssociatedClasses;
    }
}