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
//    public static <T> CreateNewInstanceBuilder<T> createNewInstance() {
//        return new CreateNewInstanceBuilder<>();
//    }
//    public static CreateNewInstancesBuilder createListOfInstances(int number) {
//        return new CreateNewInstancesBuilder(number);
//    }

//    @AllArgsConstructor
//    public static class CreateNewInstancesBuilder {
//        private final int number;
//
//        public <T> T usingGenerator(Generator<T> generator){
//            return new GeneratorSelectionBuilder(new ConfigurationBuilder()).usingGenerator(generator);
//        }
//
//        public GeneratorSelectionBuilder withConfiguration(ConfigurationBuilder configurationBuilder){
//            return new GeneratorSelectionBuilder(configurationBuilder);
//        }
//    }

//    public static class NewInstance {
//        private static final PathNode.RootPathNode rootPathNode = new PathNode.RootPathNode();
//
//        public static <T> CreateNewInstanceBuilder createInstance() {
//            BiFunction<Generator<T>, ConfigurationBuilder, T> callCreateFunction = (gen,conf) ->
//                    GeneratorAccessor.create(gen, conf.getInitializationContext(), rootPathNode);
//            return new CreateNewInstanceBuilder(callCreateFunction);
//        }
//
//        public static CreateNewInstanceBuilder createInstances(int i) {
//            return null;
//        }
//    }

//    @AllArgsConstructor
//    public static class CreateNewInstanceBuilder {
//        private final BiFunction<Generator<Object>, ConfigurationBuilder, Object> callCreateFunction;
//
//        public <T> T usingGenerator(Generator<T> generator){
//            return callCreateFunction.apply(generator, new ConfigurationBuilder());
//            return withConfiguration(new ConfigurationBuilder()).usingGenerator(generator);
//        }
//
//        public <T> GeneratorSelectionBuilder<T> withConfiguration(ConfigurationBuilder configurationBuilder){
//            Function<Generator<T>, T> callCreateFunction = gen ->
//                    GeneratorAccessor.create(gen, configurationBuilder.getInitializationContext(), rootPathNode);
//
//            return new GeneratorSelectionBuilder<T>(callCreateFunction);
//        }
//    }
//
//    @AllArgsConstructor
//    public static class GeneratorSelectionBuilder<T> {
//        private final Function<Generator<T>, T> callOnGenerator;
//
//        public T usingGenerator(Generator<T> generator){
//            return callOnGenerator.apply(generator);
//        }
//    }

    private static <T> T create(Generator<T> generator) {
        return configureRules(new InitializationContext()).andCreate(generator);
    }

    private static <T> SizeSelectionBuilder<T> createListOfInstances(Generator<T> generator) {
        return configureRules(new InitializationContext()).andCreateListOfInstances(generator);
    }

    private static CreationWithInitializationConfigBuilder configureRules(Rules rules) {
        return configureRules(new InitializationContext(rules));
    }

    private static CreationWithInitializationConfigBuilder configureRules(InitializationContext initializationContext) {
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
//        Initializer.using(Generators.instance(String.class)).createNewInstance();
//        Initializer.using(Generators.instance(String.class)).createInstances(5);


        String instance = Initializer.create(instance(String.class));
        List<String> instances = Initializer.createListOfInstances(instance(String.class)).withSize(5);
        String instance2 = Initializer.configureRules(Rules.NO_RULES).andCreate(instance(String.class));
        List<String> instances2 = Initializer.configureRules(Rules.NO_RULES).andCreateListOfInstances(instance(String.class)).withSize(10);


//        String i1 = NewInstance.createInstance().usingGenerator(instance(String.class));
//        NewInstance.createInstances(5).usingGenerator(instance(String.class));


//        String instance = Initializer.createNewInstance().usingGenerator(Generators.instance(String.class));
//        List<String> instances = Initializer.createListOfInstances(5).usingGenerator(Generators.instance(String.class));
//        String instance2 = Initializer.createNewInstance()
//                .withConfiguration(new ConfigurationBuilder()
//                        .setGlobalRules(PredefinedRules.ALL_RULES)
//                        .setRulesForPath(PathBuilder.root().addProperty("test"), Rules.NO_RULES)
//                        .setGlobalRandomValueGeneratorRules(PredefinedRules.ALL_RULES))
//                .usingGenerator(Generators.instance(String.class));
    }


}
