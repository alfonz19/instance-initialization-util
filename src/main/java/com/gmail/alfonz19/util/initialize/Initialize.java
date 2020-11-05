package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.initialize.builder.InitializationConfigurationBuilder;
import com.gmail.alfonz19.util.initialize.context.InitializationConfiguration;
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
        return withConfiguration(new InitializationConfiguration(rules));
    }

    public static InitializationUsingRules withConfiguration(InitializationConfiguration initializationConfiguration) {
        return new InitializationUsingRules(initializationConfiguration);
    }

    public static InitializationUsingRules withConfiguration(InitializationConfigurationBuilder builder) {
        return new InitializationUsingRules(builder.getinitializationConfiguration());
    }

    @AllArgsConstructor
    public static class InitializationUsingRules {

        private final InitializationConfiguration initializationConfiguration;

        public <T> T initialize(Generator<T> generator) {
            return Initializer.configureRules(initializationConfiguration).andCreate(generator);
        }

        public <T> List<T> initializeList(Generator<T> generator, int number) {
            return Initializer.configureRules(initializationConfiguration)
                    .andCreateListOfInstances(generator)
                    .withSize(number);
        }
    }
}
