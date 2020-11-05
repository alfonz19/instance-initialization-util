package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.initialize.context.InitializationConfiguration;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.GeneratorAccessor;
import com.gmail.alfonz19.util.initialize.rules.Rules;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Initializer {

    public static <T> T create(Generator<T> generator) {
        return configureRules(new InitializationConfiguration()).andCreate(generator);
    }

    public static <T> SizeSelectionBuilder<T> createListOfInstances(Generator<T> generator) {
        return configureRules(new InitializationConfiguration()).andCreateListOfInstances(generator);
    }

    public static CreationWithInitializationConfigBuilder configureRules(Rules rules) {
        return configureRules(new InitializationConfiguration(rules));
    }

    public static CreationWithInitializationConfigBuilder configureRules(InitializationConfiguration initializationConfiguration) {
        return new CreationWithInitializationConfigBuilder(initializationConfiguration);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreationWithInitializationConfigBuilder {
        private final InitializationConfiguration initializationConfiguration;

        public <T> T andCreate(Generator<T> generator) {
            return GeneratorAccessor.create(generator, initializationConfiguration, new PathNode.RootPathNode());
        }

        public <T> SizeSelectionBuilder<T> andCreateListOfInstances(Generator<T> generator) {
            return new SizeSelectionBuilder<>(generator, initializationConfiguration);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SizeSelectionBuilder<T> {
        private final Generator<T> generator;
        private final InitializationConfiguration initializationConfiguration;


        public List<T> withSize(int size) {
            return GeneratorAccessor.create(generator, size, initializationConfiguration, new PathNode.RootPathNode());
        }
    }
}
