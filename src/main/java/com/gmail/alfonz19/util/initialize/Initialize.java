package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.initialize.builder.ConfigurationBuilder;
import com.gmail.alfonz19.util.initialize.context.InitializationContext;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.GeneratorAccessor;
import com.gmail.alfonz19.util.initialize.rules.Rules;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Initialize {

    public static <T> T initialize(Generator<T> generator) {
        return new Initializer().create(generator);
    }

    public static <T> List<T> initializeList(Generator<T> generator, int number) {
        return new Initializer().create(generator, number);
    }

    public static InitializationUsingRules withConfiguration(Rules rules) {
        return withConfiguration(new InitializationContext(rules));
    }

    public static InitializationUsingRules withConfiguration(InitializationContext initializationContext) {
        return new InitializationUsingRules(initializationContext);
    }

    public static InitializationUsingRules withConfiguration(ConfigurationBuilder initializationContext) {
        return new InitializationUsingRules(initializationContext);
    }

    @AllArgsConstructor
    public static class InitializationUsingRules {

        private final InitializationContext initializationContext;

        public <T> T initialize(Generator<T> generator) {
            return GeneratorAccessor.create(generator, initializationContext, new PathNode.RootPathNode());
        }

        public <T> List<T> initializeList(Generator<T> generator, int number) {
            return GeneratorAccessor.create(generator, number, initializationContext, new PathNode.RootPathNode());
        }
    }
}
