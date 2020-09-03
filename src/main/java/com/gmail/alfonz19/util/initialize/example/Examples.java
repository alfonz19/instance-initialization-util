package com.gmail.alfonz19.util.initialize.example;

import com.gmail.alfonz19.util.initialize.example.to.AssociatedClass;
import com.gmail.alfonz19.util.initialize.example.to.InterfaceTestingClassA;
import com.gmail.alfonz19.util.initialize.example.to.InterfaceTestingClassB;
import com.gmail.alfonz19.util.initialize.example.to.RootDto;
import com.gmail.alfonz19.util.initialize.example.to.TestingInterface;
import com.gmail.alfonz19.util.initialize.generator.Generators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.list;
import static com.gmail.alfonz19.util.initialize.generator.Generators.randomInt;

//type variables, unused method parameters, unused constructs, unused result of method call, useless assignment, unused variable, unused assignment
@SuppressWarnings({"squid:S119",
        "squid:S1172",
        "unused",
        "squid:S1854",
        "squid:S1481",
        "UnusedAssignment"})
public class Examples {

    public static void main(String[] args) {
        RootDto initialized;

        //trivial creation
        initialized = Generators.instance(RootDto::new).create();

        //top-level list creation without specified List implementation, using interface as a type, having all items of same type
        List<? extends TestingInterface> listUsingXExtendsInterfaceHavingAllInstancesOfSameType =
                Generators.list(Generators.instance(InterfaceTestingClassA::new))
                        .withSize(10)
                        .create();


        //top-level list creation without specified List implementation, using interface as a type, having all items of alternating type.
        List<? extends TestingInterface> listUsingXExtendsInterfaceHavingAllInstancesOfAlternatingType =
                Generators.list(Generators.roundRobinGenerator(
                        Generators.instance(InterfaceTestingClassA::new),
                        Generators.instance(InterfaceTestingClassB::new)))
                        .withSize(10)
                        .create();


        //actually it seems that example above need not that `? extends` part, if `roundRobinGenerator` has more than 1 generator.
        List<TestingInterface> listUsingXExtendsInterfaceHavingAllInstancesOfAlternatingType2 =
                Generators.list(Generators.roundRobinGenerator(
                        Generators.instance(InterfaceTestingClassA::new),
                        Generators.instance(InterfaceTestingClassB::new)))
                        .withSize(10)
                        .create();


        //top-level list creation without specified List implementation, using interface as a type, having all items of same type
        List<TestingInterface> listUsingInterfaceHavingAllInstancesOfSameType1 =
                Generators.list(Generators.<TestingInterface>instance(InterfaceTestingClassA::new))
                        .withSize(10)
                        .create();

        //top-level list creation without specified List implementation, using interface as a type, having all items of same type, 2nd syntax
        List<TestingInterface> listUsingInterfaceHavingAllInstancesOfSameType2 =
                Generators.list(Generators.instance(TestingInterface.class, InterfaceTestingClassA::new))
                        .withSize(10)
                        .create();

        //nullifying and skipping properties
        initialized = Generators.instance(RootDto::new)
                //to get rid of instance initialization value
                .nullifyProperty(RootDto::getSomeStringValue, RootDto::getAnotherRootDtoToInit)
                //if detection of unhandled property is turned on and want just ignore this property.
                .skipProperty(RootDto::getListOfListsOfAssociatedClasses, RootDto::getSomeIntegerProperty2)
                .create();

        //support for simple setter
        initialized = Generators.instance(RootDto::new)
            .setProperty(RootDto::getSomeStringValue).toValue("value")
            .create();


        //set specific String property
        initialized = Generators.instance(RootDto::new)
                .setProperty(RootDto::getSomeStringValue).to(Generators.randomString()
                        .withSize(10).addPrefix("abc")
                        .updatedWithContext((s, context) -> s + ":: " + context.getPath().getPathAsString()))
                .create();


        //set all String properties
        initialized = Generators.instance(RootDto::new)
                .setAllPropertiesHavingType(String.class).to(
                        Generators.randomString()
                                .withSize(10).addPrefix("abc")
                                .updatedWithContext((s, context) -> s + ":: " + context.getPath().getPathAsString()))
                .create();


        //initialize nested list instance: List<AssociatedClass>
        initialized = Generators.instance(RootDto::new)
                .setProperty(RootDto::getListOfAssociatedClasses).to(
                        Generators.list(LinkedList::new, Generators.instance(AssociatedClass::new))
                                .withMinSize(5)
                                .withMaxSize(10))
                .create();


        //initialize nested list instance: List<AssociatedClass>, using alternating generators for that list.
        initialized = Generators.instance(RootDto::new)
                .setProperty(RootDto::getListInterfaces).to(
                        Generators.list(
                                Generators.roundRobinGenerator(
                                        Generators.instance(InterfaceTestingClassA::new),
                                        Generators.instance(InterfaceTestingClassB::new)))
                                .withMinSize(5)
                                .withMaxSize(10))
                .create();


        //initialize nested list instance: List<List<AssociatedClass>>. The outer list will be set to size 5-10 and implementation is LinkedList, inner list has size 3 and it's arraylist.
        initialized = Generators.instance(RootDto::new)
                .setProperty(RootDto::getListOfListsOfAssociatedClasses).to(
                        Generators.list(LinkedList::new,
                                Generators.list(ArrayList::new, Generators.instance(AssociatedClass::new))
                                        .withSize(3))
                                .withMinSize(5)
                                .withMaxSize(10))
                .create();

        //top-level list creation without specified List implementation
        List<AssociatedClass> associatedClasses =
                Generators.list(Generators.instance(AssociatedClass::new).nullifyAllProperties())
                        .withSize(10)
                        .create();

        //top-level array creation
        AssociatedClass[] associatedClassesArray =
                Generators.array(AssociatedClass.class, Generators.instance(AssociatedClass::new).nullifyAllProperties())
                        .withSize(10)
                        .create();

        //enum assignment.
        instance(RootDto::new)
                .setProperty(RootDto::getEnumerated).to(Generators.enumeratedType(RootDto.Enumerated.class).random())
                .setAllPropertiesHavingType(String.class).toNull()
                .create();

        //simplified enum specification
        instance(RootDto::new)
                .setEnumProperty(RootDto::getEnumerated).random()
                .setAllPropertiesHavingType(String.class).toNull()
                .create();
        //------------------------




        //<editor-fold desc="TESTING">

        RootDto rootDto = instance(RootDto::new)
                .setPropertyTo(RootDto::getSomeIntegerProperty, randomInt().biggerThan(10).smallerThan(20))
                .create();

        RootDto rootDto1 = instance(RootDto::new).setPropertyTo(RootDto::getListOfAssociatedClasses, list(instance(AssociatedClass::new))).create();
        RootDto rootDto2 = instance(RootDto::new).setProperty(RootDto::getListOfAssociatedClasses).to(list(instance(AssociatedClass::new))).create();
        Generators.instance(RootDto::new).setProperty(RootDto::getListOfListsOfAssociatedClasses).to(
                Generators.list(
                        Generators.list(
                                Generators.instance(AssociatedClass::new))))
                .create();

//        Generators.instance(RootDto::new).setProperty(RootDto::getListOfAssociatedClasses).to()





        //</editor-fold>

    }



//                .referingToFieldUpContextPath(AssociatedClass::getToInit, 1)
//                .referingToFieldUpContextPath(AssociatedClass::getToInit, 1, RootDto.class, RootDto::getAnotherToInit)
}
