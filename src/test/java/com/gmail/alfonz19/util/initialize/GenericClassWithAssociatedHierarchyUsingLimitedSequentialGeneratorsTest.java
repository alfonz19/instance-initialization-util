package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.testsupport.AbstractTestSingleAndMultipleInstanceCreation;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import com.gmail.alfonz19.util.initialize.selector.SpecificTypePropertySelector;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.gmail.alfonz19.util.initialize.GenericClassWithAssociatedHierarchyUsingLimitedSequentialGeneratorsTest.ParentClass;
import static com.gmail.alfonz19.util.initialize.GenericClassWithAssociatedHierarchyUsingLimitedSequentialGeneratorsTest.TestInstance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.list;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GenericClassWithAssociatedHierarchyUsingLimitedSequentialGeneratorsTest extends
        AbstractTestSingleAndMultipleInstanceCreation<TestInstance<List<ParentClass>>> {

    public static final SpecificTypePropertySelector<TestInstance<List<ParentClass>>, List<ParentClass>>
            PROPERTY_SELECTOR = TestInstance::getGenericProperty;
    public static final String STRING_CONSTANT = "YES!";

    @Override
    protected Generator<TestInstance<List<ParentClass>>> createGenerator() {
        //This is a little bit complicated. What is done here is, that we create instance of TestInstance,
        //and set associated list of T instances to newly created ArrayList of entities, having size 2.
        //To init that list, we use sequential generator, which will generate all items from first generator until it
        //can, proceeding to next one only if first generator is done. And finally the first generator is 'limited',
        //allowed to generate only 1 item and then it expires.
        //
        //So this constructs will set 2 items into field 'genericProperty', where first one will be
        //ParentClass, and second will be ChildClass.
        return Generators.<TestInstance<List<ParentClass>>>instance(TestInstance::new)
                .setProperty(PROPERTY_SELECTOR).to(
                        list(Generators.sequentialGenerator(
                                Generators.limitedGenerator(1,
                                        instance(ParentClass.class).setAllPropertiesHavingType(String.class).toValue(STRING_CONSTANT)),
                                instance(ChildClass.class).setAllPropertiesHavingType(String.class).toValue(STRING_CONSTANT))
                        ).withSize(2));

//        je potřeba zpropagovat type z instance, skrze všechny generatory až do horní instance — nějak.
    }

    @Override
    protected void assertCreatedInstance(TestInstance<List<ParentClass>> testInstance) {
        assertThat(testInstance, notNullValue());
        List<ParentClass> list = PROPERTY_SELECTOR.select(testInstance);
        assertThat(list, notNullValue());
        assertThat(list.size(), is(2));

        ParentClass firstItem = list.get(0);
        ParentClass secondItem = list.get(1);
        assertThat(firstItem, notNullValue());
        assertThat(secondItem, notNullValue());
        assertTrue(secondItem instanceof ChildClass);

        assertThat(firstItem.getA(), is(STRING_CONSTANT));
        assertThat(secondItem.getA(), is(STRING_CONSTANT));
        assertThat(((ChildClass)secondItem).getB(), is(STRING_CONSTANT));

    }

    @Setter
    @Getter
    public static class TestInstance<T> {
        T genericProperty;
    }

    @Setter
    @Getter
    public static class ParentClass {
        private String a;
    }

    @Setter
    @Getter
    public static class ChildClass extends ParentClass {
        private String b;
    }
}
