package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.PathNode;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class InitializationUsingRules {

    private final Rules rules;

    public static InitializationUsingRules withConfiguration(Rules rules) {
        return new InitializationUsingRules(rules);
    }

    public <T> T initialize(Generator<T> generator) {
        return GeneratorAccessor.create(generator, new PathNode.RootPathNode(rules));
    }

    public <T> List<T> initializeList(Generator<T> generator, int number) {
        return GeneratorAccessor.create(generator, number, new PathNode.RootPathNode(rules));
    }
}
