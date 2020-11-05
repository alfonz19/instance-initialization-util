package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.initialize.builder.ConfigurationBuilder;
import com.gmail.alfonz19.util.initialize.context.InitializationContext;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.rules.Rules;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Initialize {

    public static <T> T initialize(Generator<T> generator) {
        return Initializer.create(generator);
    }

    public static <T> List<T> initializeList(Generator<T> generator, int number) {
        return Initializer.createListOfInstances(generator).withSize(number);
    }

    public static InitializationUsingRules withConfiguration(Rules rules) {
        return withConfiguration(new InitializationContext(rules));
    }

    public static InitializationUsingRules withConfiguration(InitializationContext initializationContext) {
        return new InitializationUsingRules(initializationContext);
    }

    public static InitializationUsingRules withConfiguration(ConfigurationBuilder initializationContext) {
        return new InitializationUsingRules(initializationContext.getInitializationContext());
    }

    @AllArgsConstructor
    public static class InitializationUsingRules {

        private final InitializationContext initializationContext;

        public <T> T initialize(Generator<T> generator) {
            return Initializer.configureRules(initializationContext).andCreate(generator);
        }

        public <T> List<T> initializeList(Generator<T> generator, int number) {
            return Initializer.configureRules(initializationContext)
                    .andCreateListOfInstances(generator)
                    .withSize(number);
        }
    }
}
