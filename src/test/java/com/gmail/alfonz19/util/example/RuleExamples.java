package com.gmail.alfonz19.util.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gmail.alfonz19.util.example.to.AssociatedClass;
import com.gmail.alfonz19.util.example.to.GenericSubClass;
import com.gmail.alfonz19.util.example.to.GenericSubClassUsedInDepths;
import com.gmail.alfonz19.util.example.to.GenericSubClassUsedInDepths.A;
import com.gmail.alfonz19.util.example.to.RootDto;
import com.gmail.alfonz19.util.initialize.context.PathMatcherBuilder;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import com.gmail.alfonz19.util.initialize.generator.InitializationUsingRules;
import com.gmail.alfonz19.util.initialize.generator.Rules;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.list;
import static com.gmail.alfonz19.util.initialize.generator.InitializationUsingRules.withConfiguration;
import static com.gmail.alfonz19.util.initialize.generator.RuleBuilder.applyGenerator;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@SuppressWarnings("squid:S2699")    //no assertion in tests.
public class RuleExamples {

    @Test
    public void initAnywhereInTreeStringPropertyWhichNameContainsGivenText() throws JsonProcessingException {
        RootDto rootDto = withConfiguration(new Rules()
                .enableJpaRules()
                .addRule(applyGenerator(Generators.randomString().withSize(10).addPrefix("success"))
                        .ifPropertyClassTypeIsEqualTo(String.class)
                        .ifPathMatches(PathMatcherBuilder.root().addAnySubPath().addPropertyRegex("someString.*?"))
                ))
                .initialize(instance(RootDto.class)
                        .setProperty(RootDto::getListOfAssociatedClasses).to(list(instance(AssociatedClass::new)).withSize(1)));

        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        System.out.println(objectWriter.writeValueAsString(rootDto));
    }

    @Test
    public void crazyCyclicGenericInitializationWithDepthLimitation() {
        Rules rules = new Rules()
                .addRule(applyGenerator(instance(new TypeReference<GenericSubClass<Integer, A>>() {}))
                        .ifPropertyClassTypeIsEqualTo(GenericSubClass.class));
        GenericSubClassUsedInDepths initialize = withConfiguration(rules)
                .initialize(instance(GenericSubClassUsedInDepths.class));
        System.out.println(initialize.toString());
    }

    @Test
    public void automaticInitializationOfGenericClassHavingGenericParent() {

//        GenericSubClass<Integer, String> instance = initialize(
//                instance(GenericSubClass::new, new TypeReference<GenericSubClass<Integer, String>>() {})
//                        .setUnsetPropertiesRandomlyUsingGuessedType());

        //or

        Rules rules = new Rules()
                .addRule(
                        applyGenerator(Generators.list(Generators.randomForGuessedType(true, false)).withSize(5))
                                .ifPropertyClassTypeIsEqualTo(List.class));

        GenericSubClass<Integer, String> instance = InitializationUsingRules.withConfiguration(rules)
                .initialize(instance(new TypeReference<GenericSubClass<Integer, String>>() {})
                        .setUnsetPropertiesRandomlyUsingGuessedType());
        System.out.println(instance);

        assertThat(instance, isA(GenericSubClass.class));
        assertThat(instance.getTlist(), notNullValue());
        assertThat(instance.getTlist().size(), CoreMatchers.is(5));

        instance.getTlist().forEach(e -> {
                    assertThat(e, isA(Integer.class));
                    assertThat(e, notNullValue());
                }
        );

        assertThat(instance.getK(), notNullValue());


        assertThat(instance.getT(), notNullValue());
    }

    @Test
    public void automaticInitializationListUsingGenericRule() {

        Rules rules = new Rules()
                .addRule(
                        applyGenerator(Generators.list(Generators.randomForGuessedType(true, false)).withSize(5))
                                .ifPropertyClassTypeIsEqualTo(List.class));

        List<List<String>> instance =
                InitializationUsingRules.withConfiguration(rules)
                .initialize(list(ArrayList::new, new TypeReference<List<List<String>>>() {}));
        System.out.println(instance);

        //TODO MMUCHA: missing methods for other collections. We have this:
        // com.gmail.alfonz19.util.initialize.generator.Generators.list(java.util.function.Function<java.util.Collection<? extends ITEM_TYPE>,java.util.List<ITEM_TYPE>>, com.fasterxml.jackson.core.type.TypeReference<java.util.List<ITEM_TYPE>>)
        //add same for set and stream.



    }
}
