package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.initialize.context.InitializationContext;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.GeneratorAccessor;
import com.gmail.alfonz19.util.initialize.rules.Rules;
import lombok.AllArgsConstructor;

import java.util.List;

import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;

public class Initializer {

    public static <T> T create(Generator<T> generator) {
        return configureRules(new InitializationContext()).andCreate(generator);
    }

    public static <T> SizeSelectionBuilder<T> createListOfInstances(Generator<T> generator) {
        return configureRules(new InitializationContext()).andCreateListOfInstances(generator);
    }

    public static CreationWithInitializationConfigBuilder configureRules(Rules rules) {
        return configureRules(new InitializationContext(rules));
    }

    public static CreationWithInitializationConfigBuilder configureRules(InitializationContext initializationContext) {
        return new CreationWithInitializationConfigBuilder(initializationContext);
    }

    @AllArgsConstructor
    public static class CreationWithInitializationConfigBuilder {
        private final InitializationContext initializationContext;

        public <T> T andCreate(Generator<T> generator) {
            return GeneratorAccessor.create(generator, initializationContext, new PathNode.RootPathNode());
        }

        public <T> SizeSelectionBuilder<T> andCreateListOfInstances(Generator<T> generator) {
            return new SizeSelectionBuilder<>(generator, initializationContext);
        }
    }

    @AllArgsConstructor
    public static class SizeSelectionBuilder<T> {
        private final Generator<T> generator;
        private final InitializationContext initializationContext;


        public List<T> withSize(int size) {
            return GeneratorAccessor.create(generator, size, initializationContext, new PathNode.RootPathNode());
        }
    }

    public static void main(String[] args) {
        String instance = Initializer.create(instance(String.class));
        List<String> instances = Initializer.createListOfInstances(instance(String.class)).withSize(5);
        String instance2 = Initializer.configureRules(Rules.NO_RULES).andCreate(instance(String.class));
        List<String> instances2 = Initializer.configureRules(Rules.NO_RULES).andCreateListOfInstances(instance(String.class)).withSize(10);
    }
}
