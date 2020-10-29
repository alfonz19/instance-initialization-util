package com.gmail.alfonz19.util.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gmail.alfonz19.testsupport.InitializedInstanceLogger;
import com.gmail.alfonz19.util.example.to.ClassImplementingInterfaceA;
import com.gmail.alfonz19.util.example.to.ClassImplementingInterfaceB;
import com.gmail.alfonz19.util.example.to.ClassWithIntegerProperties;
import com.gmail.alfonz19.util.example.to.ClassWithMultipleStringProperties;
import com.gmail.alfonz19.util.example.to.ClassWithOneEnumProperty;
import com.gmail.alfonz19.util.example.to.ClassWithOneStringProperty;
import com.gmail.alfonz19.util.example.to.ClassWithSupplierProperty;
import com.gmail.alfonz19.util.example.to.EnumImplementingInterface;
import com.gmail.alfonz19.util.example.to.GenericClass;
import com.gmail.alfonz19.util.example.to.GenericSubClass;
import com.gmail.alfonz19.util.example.to.RootDto;
import com.gmail.alfonz19.util.example.to.TestingInterface;
import com.gmail.alfonz19.util.initialize.generator.Generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Rule;
import org.junit.Test;

import static com.gmail.alfonz19.util.initialize.Initialize.initialize;
import static com.gmail.alfonz19.util.initialize.generator.Generators.array;
import static com.gmail.alfonz19.util.initialize.generator.Generators.defaultValue;
import static com.gmail.alfonz19.util.initialize.generator.Generators.enumeratedType;
import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.list;
import static com.gmail.alfonz19.util.initialize.generator.Generators.randomInt;
import static com.gmail.alfonz19.util.initialize.generator.Generators.randomString;
import static com.gmail.alfonz19.util.initialize.generator.Generators.roundRobinGenerator;
import static com.gmail.alfonz19.util.initialize.generator.Generators.set;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

//type variables, unused method parameters, unused constructs, unused result of method call, useless assignment, unused variable
@SuppressWarnings({"squid:S119",
        "squid:S1172",
        "unused",
        "squid:S1854",
        "squid:S1481"})
public class ExampleUsageTest {

    @Rule
    public InitializedInstanceLogger initializedInstanceLogger = new InitializedInstanceLogger();

    //<editor-fold desc="Basic initializations">
    @Test
    public void testInstanceCreationUsingSupplier() {
        ClassWithOneStringProperty initialized = initialize(instance(ClassWithOneStringProperty::new));
        initializedInstanceLogger.logInitializedInstance(initialized);

        //instance was created.
        assertThat(initialized, notNullValue());

        //property is not set.
        assertThat(initialized.getStringProperty(), nullValue());
    }

    @Test
    public void testInstanceCreationUsingClass() {
        ClassWithOneStringProperty initialized = initialize(instance(ClassWithOneStringProperty.class));
        initializedInstanceLogger.logInitializedInstance(initialized);

        //instance was created.
        assertThat(initialized, notNullValue());

        //property is not set.
        assertThat(initialized.getStringProperty(), nullValue());
    }

    @Test
    public void testInstanceCreationWhenInstantiatingSubClassOfReturnedType() {
        TestingInterface initialized = initialize(instance(TestingInterface.class, ClassImplementingInterfaceA::new));
        initializedInstanceLogger.logInitializedInstance(initialized);

        //instance was created.
        assertThat(initialized, notNullValue());
        assertEquals(initialized.getClass(), ClassImplementingInterfaceA.class);

        //property is not set.
        assertThat(((ClassImplementingInterfaceA)initialized).getStringProperty(), nullValue());
    }
    //</editor-fold>

    //———————————————————————————————————————————————————————————————

    //<editor-fold desc="Collection-like initializations">
    @Test
    public void testListInitializationWithoutSpecifiedListImplementation() {
        //top-level list creation without specified List implementation, having all items of same type
        List<ClassImplementingInterfaceA> initialized =
                initialize(list(instance(ClassImplementingInterfaceA::new)).withSize(4));
        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertThat(initialized, isA(List.class));
        assertEquals(initialized.getClass(), ArrayList.class);
        assertThat(initialized.size(), is(4));
        initialized.forEach(item -> {
            assertEquals(item.getClass(), ClassImplementingInterfaceA.class);
            assertThat(item.getStringProperty(), nullValue());
        });
    }

    @Test
    public void testListInitializationWithoutSpecifiedListImplementationWithItemPropertiesRandomlyInitialized() {
        //top-level list creation without specified List implementation, having all items of same type
        List<ClassImplementingInterfaceA> initialized =
                initialize(list(instance(ClassImplementingInterfaceA::new).setUnsetPropertiesRandomly()).withSize(4));
        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertThat(initialized, isA(List.class));
        assertEquals(initialized.getClass(), ArrayList.class);
        assertThat(initialized.size(), is(4));
        initialized.forEach(item -> {
            assertEquals(item.getClass(), ClassImplementingInterfaceA.class);
            assertThat(item.getStringProperty(), notNullValue());
        });
    }

    @Test
    public void testListInitializationWithoutSpecifiedListImplementationIntoListVariableUsingInterface() {
        //top-level list creation without specified List implementation, using interface as a type, having all items of same type
        List<? extends TestingInterface> initialized =
                initialize(list(instance(ClassImplementingInterfaceA::new)).withSize(4));
        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertThat(initialized, isA(List.class));
        assertEquals(initialized.getClass(), ArrayList.class);
        assertThat(initialized.size(), is(4));
        initialized.forEach(item -> {
            assertEquals(item.getClass(), ClassImplementingInterfaceA.class);
            assertThat(((ClassImplementingInterfaceA)item).getStringProperty(), nullValue());
        });
    }

    @Test
    public void testListInitializationIntoListVariableUsingInterfaceWithAvoidingUsingExtendsKeywordByProvidingTypeValue() {
        //top-level list creation without specified List implementation, using interface as a type, having all items of same type
        //we provide generic type to instance call to avoid getting type List<? extends TestingInterface>
        List<TestingInterface> initialized =
                initialize(list(Generators.<TestingInterface>instance(ClassImplementingInterfaceA::new)).withSize(4));

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertThat(initialized, isA(List.class));
        assertEquals(initialized.getClass(), ArrayList.class);
        assertThat(initialized.size(), is(4));
        initialized.forEach(item -> {
            assertEquals(item.getClass(), ClassImplementingInterfaceA.class);
            assertThat(((ClassImplementingInterfaceA)item).getStringProperty(), nullValue());
        });
    }

    @Test
    public void testListInitializationIntoListVariableUsingInterfaceWithAvoidingUsingExtendsKeywordByProvidingClassType() {
        //top-level list creation without specified List implementation, using interface as a type, having all items of same type, 2nd syntax
        List<TestingInterface> initialized =
                initialize(list(instance(TestingInterface.class, ClassImplementingInterfaceA::new))
                        .withSize(4));

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertThat(initialized, isA(List.class));
        assertEquals(initialized.getClass(), ArrayList.class);
        assertThat(initialized.size(), is(4));
        initialized.forEach(item -> {
            assertEquals(item.getClass(), ClassImplementingInterfaceA.class);
            assertThat(((ClassImplementingInterfaceA)item).getStringProperty(), nullValue());
        });
    }

    @Test
    public void testListInitializationWithSpecifiedListImplementationIntoListVariableUsingInterface() {
        //top-level list creation with specified LinkedList implementation, using interface as a type, having all items of same type
        List<? extends TestingInterface> initialized =
                initialize(list(LinkedList::new, instance(ClassImplementingInterfaceA::new)).withSize(4));
        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertThat(initialized, isA(List.class));
        assertEquals(initialized.getClass(), LinkedList.class);
        assertThat(initialized.size(), is(4));
        initialized.forEach(item -> {
            assertEquals(item.getClass(), ClassImplementingInterfaceA.class);
            assertThat(((ClassImplementingInterfaceA)item).getStringProperty(), nullValue());
        });
    }

    @Test
    public void testCreationOfAlternatingItemTypesUsingRoundRobinGenerator() {
        //top-level list creation without specified List implementation, using interface as a type,
        //having all items of alternating type.

        List<TestingInterface> initialized =
                initialize(list(roundRobinGenerator(
                        instance(ClassImplementingInterfaceA::new).setUnsetPropertiesRandomly(),
                        instance(ClassImplementingInterfaceB::new)))
                        .withSize(4));
        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertThat(initialized, isA(List.class));
        assertEquals(initialized.getClass(), ArrayList.class);
        assertThat(initialized.size(), is(4));

        for (int i = 0, initializedSize = initialized.size(); i < initializedSize; i++) {
            TestingInterface item = initialized.get(i);
            if (i%2==0) {
                assertEquals(item.getClass(), ClassImplementingInterfaceA.class);
                assertThat(((ClassImplementingInterfaceA) item).getStringProperty(), notNullValue());
            } else {
                assertEquals(item.getClass(), ClassImplementingInterfaceB.class);
                assertThat(((ClassImplementingInterfaceB) item).getIntegerProperty(), nullValue());
            }
        }
    }

    @Test
    public void testCreationUsingRoundRobinGeneratorWhenJustOneGeneratorIsProvided() {
        //top-level list creation without specified List implementation, using interface as a type,
        //having all items of alternating type.

        //notice, that if just 1 item is passed to roundRobinGenerator (not typical usage), we need to use `? extends` in resultant type
        //or if we want just List<TestingInterface> we need to provide classType to instance calls, is instance(TestingInterface.class, ...)
        //or instance has to provided with actual type.
        List<? extends TestingInterface> initialized =
                initialize(list(roundRobinGenerator(instance(ClassImplementingInterfaceB::new))).withSize(4));

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertThat(initialized, isA(List.class));
        assertEquals(initialized.getClass(), ArrayList.class);
        assertThat(initialized.size(), is(4));

        initialized.forEach(item -> {
            assertEquals(item.getClass(), ClassImplementingInterfaceB.class);
            assertThat(((ClassImplementingInterfaceB) item).getIntegerProperty(), nullValue());
        });
    }

    @Test
    public void testSetInitializationWithAllItemsEqual() {
        //top-level list creation without specified List implementation, having all items of same type
        Set<ClassImplementingInterfaceA> initialized =
                initialize(set(instance(ClassImplementingInterfaceA::new)).withSize(4));
        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertThat(initialized, isA(Set.class));
        assertEquals(initialized.getClass(), HashSet.class);
        assertThat(initialized.size(), is(1));
        initialized.forEach(item -> {
            assertEquals(item.getClass(), ClassImplementingInterfaceA.class);
            assertThat(item.getStringProperty(), nullValue());
        });
    }

    @Test
    public void testSetInitialization() {
        //top-level list creation without specified List implementation, having all items of same type
        Set<ClassImplementingInterfaceA> initialized =
                initialize(set(instance(ClassImplementingInterfaceA::new).setUnsetPropertiesRandomly()).withSize(4));
        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertThat(initialized, isA(Set.class));
        assertEquals(initialized.getClass(), HashSet.class);
        assertThat(initialized.size(), is(4));
        initialized.forEach(item -> {
            assertEquals(item.getClass(), ClassImplementingInterfaceA.class);
            assertThat(item.getStringProperty(), notNullValue());
        });
    }

    @Test
    public void testStreamInitialization() {
        //top-level list creation without specified List implementation, having all items of same type
        Stream<ClassImplementingInterfaceA> initialized;
        initialized = initialize(Generators.stream(instance(ClassImplementingInterfaceA::new).setUnsetPropertiesRandomly()).withSize(4));

        assertThat(initialized, notNullValue());
        assertThat(initialized, isA(Stream.class));

        List<ClassImplementingInterfaceA> collected = initialized.collect(Collectors.toList());
        initializedInstanceLogger.logInitializedInstance(collected);

        assertThat(collected.size(), is(4));
        collected.forEach(item -> {
            assertEquals(item.getClass(), ClassImplementingInterfaceA.class);
            assertThat(item.getStringProperty(), notNullValue());
        });
    }

    @Test
    public void testArrayInitialization() {
        //top-level array creation
        ClassWithOneStringProperty[] initialized =
                initialize(array(ClassWithOneStringProperty.class, instance(ClassWithOneStringProperty::new).setUnsetPropertiesRandomly())
                        .withSize(4));

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());

        assertTrue(initialized.getClass().isArray());
        assertThat(initialized.length, is(4));
        Arrays.stream(initialized).forEach(item-> {
            assertThat(item, notNullValue());
            assertThat(item.getStringProperty(), notNullValue());
        });
    }

    @Test
    public void testArrayInitialization2() {
        //top-level array creation
        ClassWithOneStringProperty[] initialized =
                initialize(array(instance(ClassWithOneStringProperty::new).setUnsetPropertiesRandomly())
                        .withSize(4));

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());

        assertTrue(initialized.getClass().isArray());
        assertThat(initialized.length, is(4));
        Arrays.stream(initialized).forEach(item-> {
            assertThat(item, notNullValue());
            assertThat(item.getStringProperty(), notNullValue());
        });
    }

    @Test
    public void testArrayInitializationWithSubclasses() {
        //top-level array creation
        TestingInterface[] initialized =
                initialize(array(instance(ClassImplementingInterfaceA::new).setUnsetPropertiesRandomly())
                        .withSize(4));

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());

        assertTrue(initialized.getClass().isArray());
        assertThat(initialized.length, is(4));
        Arrays.stream(initialized).forEach(item-> {
            assertThat(item, notNullValue());
            assertEquals(item.getClass(), ClassImplementingInterfaceA.class);
        });
    }
    //</editor-fold>

    //———————————————————————————————————————————————————————————————

    //<editor-fold desc="Initializing specific properties">
    @Test
    public void testSettingValueOfPropertyDescriptor() {
        //support for simple setter
        ClassWithOneStringProperty initialized = initialize(instance(ClassWithOneStringProperty::new)
                .setProperty(ClassWithOneStringProperty::getStringProperty).toValue("value"));

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertThat(initialized.getStringProperty(), is("value"));

    }

    @Test
    public void testSettingValueOfPropertyDescriptorToValueGeneratedByStringGenerator() {
        //set specific String property
        ClassWithOneStringProperty initialized = initialize(instance(ClassWithOneStringProperty::new)
                .setProperty(ClassWithOneStringProperty::getStringProperty)
                .to(randomString()
                        .withSize(10)
                        .addPrefix("string")
                        .updatedWithContext((s, context) -> s + " at path: " + context.getPath().getPathAsString())));

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertTrue(initialized.getStringProperty().matches("string\\d{10} at path: \\.stringProperty"));
        assertThat(initialized.getStringProperty().length(), is(41));
    }

    @Test
    public void testSettingValueOfAllStringsToValueGeneratedByStringGenerator() {
        //set all String properties
        ClassWithMultipleStringProperties initialized = initialize(instance(ClassWithMultipleStringProperties::new)
                .setAllPropertiesHavingType(String.class)
                .to(randomString().withSize(10).addPrefix("abc")));

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertTrue(initialized.getStringPropertyA().matches("^abc\\d{10}$"));
        assertTrue(initialized.getStringPropertyB().matches("^abc\\d{10}$"));
        assertTrue(initialized.getStringPropertyC().matches("^abc\\d{10}$"));

    }

    @Test
    public void testSettingValueOfPropertyDescriptorToRandomEnumValueWithVerboseSyntax() {
        //enum assignment, needlessly verbose variant.
        ClassWithOneEnumProperty initialized = initialize(instance(ClassWithOneEnumProperty::new)
                .setProperty(ClassWithOneEnumProperty::getEnumProperty)
                .to(enumeratedType(ClassWithOneEnumProperty.E.class).random()));

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized.getEnumProperty(), notNullValue());
    }

    @Test
    public void testSettingValueOfPropertyDescriptorToRandomEnumValueWithShorterSyntax() {
        //enum assignment.
        ClassWithOneEnumProperty initialized = initialize(instance(ClassWithOneEnumProperty::new)
                .setEnumProperty(ClassWithOneEnumProperty::getEnumProperty).random());

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized.getEnumProperty(), notNullValue());
    }

    @Test
    public void testSettingValueOfPropertyDescriptorToRandomEnumFromGivenList() {
        //simplified enum specification
        ClassWithOneEnumProperty initialized = initialize(instance(ClassWithOneEnumProperty::new)
                .setEnumProperty(ClassWithOneEnumProperty::getEnumProperty)
                .randomFrom(ClassWithOneEnumProperty.E.A, ClassWithOneEnumProperty.E.B));

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized.getEnumProperty(), notNullValue());
        assertThat(initialized.getEnumProperty(), anyOf(
                is(ClassWithOneEnumProperty.E.A),
                is(ClassWithOneEnumProperty.E.B)));

    }

    @Test
    public void testInitializingRawUsageOfGenericTypeImplementedByEnumWhichWasRandomlyChosen() {
        //noinspection rawtypes  //this is limitation of this usage, generic type cannot be guessed.
        Supplier supplier = initialize(enumeratedType(EnumImplementingInterface.class, Supplier.class));

        assertThat(supplier, notNullValue());
        assertThat(supplier.get(), anyOf(is("A"), is("B")));
    }

    @Test
    public void testInitializingGenericTypeImplementedByEnumWhichWasRandomlyChosen() {
        Supplier<String> supplier1 =
                initialize(enumeratedType(EnumImplementingInterface.class, new TypeReference<Supplier<String>>() {}));
        assertThat(supplier1.get(), anyOf(is("A"), is("B")));

        Supplier<String> supplier2 =
                initialize(enumeratedType(EnumImplementingInterface.class, new TypeReference<Supplier<String>>() {})
                        .randomFrom(EnumImplementingInterface.A));
        assertThat(supplier2.get(), is("A"));
    }

    @Test
    public void testSettingValueOfPropertyDescriptorToGenericInterfaceImplementedByEnumWhichWasRandomlyChosen() {
        ClassWithSupplierProperty instance = initialize(instance(ClassWithSupplierProperty.class)
                .setProperty(ClassWithSupplierProperty::getSupplier)
                .to(enumeratedType(EnumImplementingInterface.class, new TypeReference<Supplier<String>>() {})));

        assertThat(instance.getSupplier().get(), anyOf(is("A"), is("B")));
    }

    @Test
    public void testSettingValueOfPropertyDescriptorsToRandomIntegers() {
        ClassWithIntegerProperties initialized = initialize(
                instance(ClassWithIntegerProperties::new)
                        .setPropertyTo(ClassWithIntegerProperties::getIntegerProperty, randomInt().biggerThan(10).smallerThan(20))
                        .setPropertyTo(ClassWithIntegerProperties::getIntProperty, randomInt().biggerThan(10).smallerThan(20))
        );

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertThat(initialized.getIntegerProperty(), allOf(notNullValue(), greaterThanOrEqualTo(10), lessThanOrEqualTo(20)));
        assertThat(initialized.getIntProperty(), allOf(notNullValue(), greaterThanOrEqualTo(10), lessThanOrEqualTo(20)));
    }

    @Test
    public void testSettingValueOfPropertyDescriptorsToDefaultValues() {
        ClassWithIntegerProperties initialized = initialize(
                instance(ClassWithIntegerProperties::new)
                        .setPropertyTo(ClassWithIntegerProperties::getIntegerProperty, defaultValue())
                        .setPropertyTo(ClassWithIntegerProperties::getIntProperty, defaultValue())
        );

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertThat(initialized.getIntegerProperty(), nullValue());
        assertThat(initialized.getIntProperty(), is(0));
    }

    @Test
    public void nestedListInitializationWithAlternatingGenerators() {
        //initialize nested list instance: List<AssociatedClass>, using alternating generators for that list.
        RootDto initialized = initialize(instance(RootDto::new)
                .setProperty(RootDto::getListInterfaces).to(
                        list(
                                roundRobinGenerator(
                                        instance(ClassImplementingInterfaceA::new).setUnsetPropertiesRandomly(),
                                        instance(ClassImplementingInterfaceB::new).setUnsetPropertiesRandomly()))
                                .withMinSize(5)
                                .withMaxSize(10)));

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertThat(initialized.getListInterfaces(), notNullValue());
        assertEquals(initialized.getListInterfaces().getClass(), ArrayList.class);

        List<TestingInterface> listInterfaces = initialized.getListInterfaces();
        assertThat(listInterfaces.size(), allOf(lessThanOrEqualTo(10), greaterThanOrEqualTo(5)));
        for(int i = 0; i < listInterfaces.size(); i++) {

            TestingInterface ith = listInterfaces.get(i);
            assertThat(ith, notNullValue());
            if (i % 2 == 0) {
                assertEquals(ith.getClass(), ClassImplementingInterfaceA.class);
            } else {
                assertEquals(ith.getClass(), ClassImplementingInterfaceB.class);
            }
        }

    }

    @Test
    public void testNullifyingAndSkippingProperties() {

        Supplier<ClassWithMultipleStringProperties> instanceSupplier = () -> {
            //this is custom instance creation; it can do more useful stuff,
            //here we abusing it to create initial state to work on.
            ClassWithMultipleStringProperties result = new ClassWithMultipleStringProperties();
            result.setStringPropertyA("A");
            return result;
        };

        ClassWithMultipleStringProperties initialized = initialize(instance(instanceSupplier)
                //to nullify any value which might be present there after initialization.
                .nullifyProperty(ClassWithMultipleStringProperties::getStringPropertyA)
                //to skip this property. After this action this property will be flagged as 'processed'
                .skipProperty(ClassWithMultipleStringProperties::getStringPropertyB)

                //initialize randomly all unprocessed properties.
                .setUnsetPropertiesRandomly()
        );

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());

        //custom supplier set this value, but initializer had rule to null it.
        assertThat(initialized.getStringPropertyA(), nullValue());

        //stringPropertyB wasn't set, but it was skipped, thus setting all randomly should skip it.
        assertThat(initialized.getStringPropertyB(), nullValue());

        assertThat(initialized.getStringPropertyC(), notNullValue());
    }
    //</editor-fold>

    //———————————————————————————————————————————————————————————————
    //<editor-fold desc="Generic Types">
    @Test
    public void testRandomInitializationOfGenericClassWithCustomSupplier() {
        GenericClass<Integer> initialized =
                initialize(
                        instance(GenericClass::new, new TypeReference<GenericClass<Integer>>() {})
                        .setUnsetPropertiesRandomly());

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertThat(initialized.getT(), notNullValue());
        assertThat(initialized.getT(), isA(Integer.class));
    }

    @Test
    public void testRandomInitializationOfGenericClassWithCalculatedSupplier() {
        GenericClass<Integer> initialized =
                initialize(instance(new TypeReference<GenericClass<Integer>>() {}).setUnsetPropertiesRandomly());

        initializedInstanceLogger.logInitializedInstance(initialized);

        assertThat(initialized, notNullValue());
        assertThat(initialized.getT(), notNullValue());
        assertThat(initialized.getT(), isA(Integer.class));
    }

    /**
     * Creates generic GenericSubClass using given types `<Integer, String>`, automatically initializing any
     * List property with 5 items. In this case the List contains items of generic type.
     */
    @Test
    public void testInitializationOfGenericClassHavingGenericParent() {
        GenericSubClass<Integer, String> instance =
                initialize(instance(new TypeReference<GenericSubClass<Integer, String>>() {})
                        .setUnsetPropertiesRandomly());

        initializedInstanceLogger.logInitializedInstance(instance);

        assertThat(instance, isA(GenericSubClass.class));
        assertThat(instance.getTlist(), nullValue());
        assertThat(instance.getK(), notNullValue());
        assertThat(instance.getK(), isA(String.class));

        assertThat(instance.getT(), notNullValue());
    }
    //</editor-fold>
}
