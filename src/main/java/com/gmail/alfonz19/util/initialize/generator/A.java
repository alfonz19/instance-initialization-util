package com.gmail.alfonz19.util.initialize.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gmail.alfonz19.util.example.to.AssociatedClass;
import com.gmail.alfonz19.util.example.to.RootDto;
import com.gmail.alfonz19.util.initialize.context.PathMatcherBuilder;

import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.list;
import static com.gmail.alfonz19.util.initialize.generator.InitializationUsingRules.withConfiguration;
import static com.gmail.alfonz19.util.initialize.generator.RuleBuilder.applyGenerator;

public class A {
    public static void main(String[] args) throws JsonProcessingException {
        RootDto rootDto = withConfiguration(new Rules()
                .enableJpaRules()
                .addRule(applyGenerator(Generators.randomString().withSize(10).addPrefix("success"))
                        .ifPropertyClassTypeIsEqualTo(String.class)
                        .ifPathMatches(PathMatcherBuilder.root().addAnySubPath().addPropertyRegex(".*1"))
                ))
                .initialize(instance(RootDto.class)
                        .setProperty(RootDto::getListOfAssociatedClasses).to(list(instance(AssociatedClass::new)).withSize(1)));

        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        System.out.println(objectWriter.writeValueAsString(rootDto));
    }
}
