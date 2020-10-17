package com.gmail.alfonz19.util.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gmail.alfonz19.util.example.to.AssociatedClass;
import com.gmail.alfonz19.util.example.to.GenericClass;
import com.gmail.alfonz19.util.example.to.InterfaceTestingClassA;
import com.gmail.alfonz19.util.example.to.InterfaceTestingClassB;
import com.gmail.alfonz19.util.example.to.RootDto;
import com.gmail.alfonz19.util.example.to.TestingInterface;
import com.gmail.alfonz19.util.initialize.generator.Generators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import static com.gmail.alfonz19.util.initialize.generator.Generators.array;
import static com.gmail.alfonz19.util.initialize.generator.Generators.enumeratedType;
import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.list;
import static com.gmail.alfonz19.util.initialize.generator.Generators.randomInt;
import static com.gmail.alfonz19.util.initialize.generator.Generators.randomString;
import static com.gmail.alfonz19.util.initialize.generator.Generators.roundRobinGenerator;
import static com.gmail.alfonz19.util.initialize.generator.Initialize.initialize;
import static com.gmail.alfonz19.util.initialize.generator.Initialize.initializeList;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

//type variables, unused method parameters, unused constructs, unused result of method call, useless assignment, unused variable, no assertion in tests.
@SuppressWarnings({"squid:S119",
        "squid:S1172",
        "unused",
        "squid:S1854",
        "squid:S1481",
        "squid:S2699"})
public class Examples {

    @Test
    public void trivialCreation() {
        RootDto initialized = initialize(instance(RootDto::new));
    }

    @Test
    public void simpleSetter() {
        //support for simple setter
        initialize(instance(RootDto::new)
                .setProperty(RootDto::getSomeStringValue).toValue("value"));
    }

    @Test
    public void propertySetterUsingStringGenerator() {
        //set specific String property
        initialize(instance(RootDto::new)
                .setProperty(RootDto::getSomeStringValue).to(randomString()
                        .withSize(10).addPrefix("abc")
                        .updatedWithContext((s, context) -> s + ":: " + context.getPath().getPathAsString())));
    }

    @Test
    public void settingAllStringProperties() {
        //set all String properties
        initialize(instance(RootDto::new)
                .setAllPropertiesHavingType(String.class).to(
                        randomString()
                                .withSize(10).addPrefix("abc")
                                .updatedWithContext((s, context) -> s + ":: " + context.getPath().getPathAsString())));
    }

    @Test
    public void enumAssignment() {
        //enum assignment.
        initialize(instance(RootDto::new)
                .setProperty(RootDto::getEnumerated).to(enumeratedType(RootDto.Enumerated.class).random())
                .setAllPropertiesHavingType(String.class).toNull());
    }

    @Test
    public void enumAssignment2() {
        //simplified enum specification
        initialize(instance(RootDto::new)
                .setEnumProperty(RootDto::getEnumerated).random()
                .setAllPropertiesHavingType(String.class).toNull());
    }

    @Test
    public void randomIntAssignment() {
        RootDto rootDto = initialize(instance(RootDto::new)
                .setPropertyTo(RootDto::getSomeIntegerProperty, randomInt().biggerThan(10).smallerThan(20)));
    }

    @Test
    public void creationWithoutSpecifiedListImplementation() {
        //top-level list creation without specified List implementation, using interface as a type, having all items of same type
        List<? extends TestingInterface> listUsingXExtendsInterfaceHavingAllInstancesOfSameType =
                initialize(list(instance(InterfaceTestingClassA::new)).withSize(10));

        List<List<InterfaceTestingClassA>> lists =
                initializeList(list(instance(InterfaceTestingClassA::new)).withSize(10), 50);
    }

    @Test
    public void creationOfAlternatingItemTypes() {
        //top-level list creation without specified List implementation, using interface as a type,
        //having all items of alternating type.
        List<? extends TestingInterface> listUsingXExtendsInterfaceHavingAllInstancesOfAlternatingType =
                initialize(list(roundRobinGenerator(
                        instance(InterfaceTestingClassA::new),
                        instance(InterfaceTestingClassB::new)))
                        .withSize(10));
    }

    @Test
    public void creationOfAlternatingItemTypesWithExtendsInterface() {
        //actually it seems that example above need not that `? extends` part, if `roundRobinGenerator` has more than 1 generator.
        List<TestingInterface> listUsingXExtendsInterfaceHavingAllInstancesOfAlternatingType2 =
                initialize(list(roundRobinGenerator(
                        instance(InterfaceTestingClassA::new),
                        instance(InterfaceTestingClassB::new)))
                        .withSize(10));
    }

    @Test
    public void testListOfInterfaces() {
        //top-level list creation without specified List implementation, using interface as a type, having all items of same type
        List<TestingInterface> listUsingInterfaceHavingAllInstancesOfSameType1 =
                initialize(list(Generators.<TestingInterface>instance(InterfaceTestingClassA::new))
                        .withSize(10));
    }

    @Test
    public void testListOfInterfaces2() {
        //top-level list creation without specified List implementation, using interface as a type, having all items of same type, 2nd syntax
        List<TestingInterface> listUsingInterfaceHavingAllInstancesOfSameType2 =
                initialize(list(instance(TestingInterface.class, InterfaceTestingClassA::new))
                        .withSize(10));
    }

    @Test
    public void nullifyingAndSkippingProperties() {
        //nullifying and skipping properties
        initialize(instance(RootDto::new)
                //to get rid of instance initialization value
                .nullifyProperty(RootDto::getSomeStringValue, RootDto::getAnotherRootDtoToInit)
                //if detection of unhandled property is turned on and want just ignore this property.
                .skipProperty(RootDto::getListOfListsOfAssociatedClasses, RootDto::getSomeIntegerProperty2));
    }

    @Test
    public void nestedListInitialization() {
        //initialize nested list instance: List<AssociatedClass>
        initialize(instance(RootDto::new)
                .setProperty(RootDto::getListOfAssociatedClasses).to(
                        list(LinkedList::new, instance(AssociatedClass::new))
                                .withMinSize(5)
                                .withMaxSize(10)));
    }

    @Test
    public void nestedListInitializationWithAlternatingGenerators() {
        //initialize nested list instance: List<AssociatedClass>, using alternating generators for that list.
        initialize(instance(RootDto::new)
                .setProperty(RootDto::getListInterfaces).to(
                        list(
                                roundRobinGenerator(
                                        instance(InterfaceTestingClassA::new),
                                        instance(InterfaceTestingClassB::new)))
                                .withMinSize(5)
                                .withMaxSize(10)));
    }

    @Test
    public void multipleNestedListInitializationsUsingDifferentListImplementations() {
        //initialize nested list instance: List<List<AssociatedClass>>. The outer list will be set to size 5-10 and implementation is LinkedList, inner list has size 3 and it's arraylist.
        initialize(instance(RootDto::new)
                .setProperty(RootDto::getListOfListsOfAssociatedClasses).to(
                        list(LinkedList::new,
                                list(ArrayList::new, instance(AssociatedClass::new))
                                        .withSize(3))
                                .withMinSize(5)
                                .withMaxSize(10)));
    }

    @Test
    public void nullifyingPropertiesInNestedClasses() {
        //top-level list creation without specified List implementation
        List<AssociatedClass> associatedClasses =
                initialize(list(instance(AssociatedClass::new).nullifyAllProperties())
                        .withSize(10));
    }

    @Test
    public void topLevelArrayCreation() {
        //top-level array creation
        AssociatedClass[] associatedClassesArray =
                initialize(array(AssociatedClass.class, instance(AssociatedClass::new).nullifyAllProperties())
                        .withSize(10));
    }

    @Test
    public void topLevelArrayCreation2() {
        //top-level array creation
        AssociatedClass[] associatedClassesArray =
                initialize(array(instance(AssociatedClass::new).nullifyAllProperties())
                        .withSize(10));
    }

    @Test
    public void unsorted() {
        RootDto rootDto1 = initialize(instance(RootDto::new).setPropertyTo(RootDto::getListOfAssociatedClasses, list(instance(AssociatedClass::new))));
        RootDto rootDto2 = initialize(instance(RootDto::new).setProperty(RootDto::getListOfAssociatedClasses).to(list(instance(AssociatedClass::new))));
        initialize(instance(RootDto::new).setProperty(RootDto::getListOfListsOfAssociatedClasses).to(
                list(
                        list(
                                instance(AssociatedClass::new))))
        );
    }

    @Test
    public void automaticInitializationOfGenericClass() {
        GenericClass<Integer> instance =
                initialize(
                        instance(GenericClass::new, new TypeReference<GenericClass<Integer>>() {})
                        .setUnsetPropertiesRandomlyUsingGuessedType());
        System.out.println(instance);
        assertThat(instance.getT(), notNullValue());
    }

    @Test
    public void name() {
        initialize(instance(RootDto::new, new TypeReference<RootDto>() {}).setUnsetPropertiesRandomlyUsingGuessedType());
    }
}
