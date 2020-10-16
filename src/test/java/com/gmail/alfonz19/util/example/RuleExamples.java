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
import com.gmail.alfonz19.util.initialize.generator.Rules;

import org.junit.Test;

import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.list;
import static com.gmail.alfonz19.util.initialize.generator.InitializationUsingRules.withConfiguration;
import static com.gmail.alfonz19.util.initialize.generator.RuleBuilder.applyGenerator;

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
        System.out.println(initialize.toString());;
    }
}
