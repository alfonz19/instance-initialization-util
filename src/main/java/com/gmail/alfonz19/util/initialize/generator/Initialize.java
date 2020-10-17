package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.PathNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Initialize {

    public static <T> T initialize(AbstractGenerator<T> generator) {
        return initialize(generator, new PathNode.RootPathNode());
    }

    public static <T> T initialize(AbstractGenerator<T> generator, PathNode pathNode) {
        return generator.create(pathNode);
    }

    public static <T> List<T> initializeList(AbstractGenerator<T> generator, int number) {
        return generator.create(number, new PathNode.RootPathNode());
    }

    public static <T> List<T> initializeList(AbstractGenerator<T> generator, int number, PathNode pathNode) {
        return generator.create(number, pathNode);
    }
}
