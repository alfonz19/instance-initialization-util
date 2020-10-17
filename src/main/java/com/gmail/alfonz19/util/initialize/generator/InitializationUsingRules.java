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

    public <T> T initialize(AbstractGenerator<T> generator) {
        return generator.create(new PathNode.RootPathNode(rules));
    }

    public <T> List<T> initializeList(AbstractGenerator<T> generator, int number) {
        return generator.create(number, new PathNode.RootPathNode(rules));
    }
}
