package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.initialize.context.InitializationContext;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.GeneratorAccessor;
import com.gmail.alfonz19.util.initialize.rules.Rules;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class InitializationUsingRules {

    private final InitializationContext initializationContext;

    public static InitializationUsingRules withConfiguration(Rules rules) {
        return withConfiguration(new InitializationContext(rules));
    }

    public static InitializationUsingRules withConfiguration(InitializationContext initializationContext) {
        return new InitializationUsingRules(initializationContext);
    }

    public <T> T initialize(Generator<T> generator) {
        return GeneratorAccessor.create(generator, initializationContext, new PathNode.RootPathNode());
    }

    public <T> List<T> initializeList(Generator<T> generator, int number) {
        return GeneratorAccessor.create(generator, number, initializationContext, new PathNode.RootPathNode());
    }
}
