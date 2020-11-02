package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.initialize.context.InitializationContext;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.GeneratorAccessor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Initialize {

    public static <T> T initialize(Generator<T> generator) {
        return initialize(generator, new InitializationContext(), new PathNode.RootPathNode());
    }

    //TODO MMUCHA: inline.
    public static <T> T initialize(Generator<T> generator,
                                   InitializationContext initializationContext,
                                   PathNode pathNode) {
        return GeneratorAccessor.create(generator, initializationContext, pathNode);
    }

    public static <T> List<T> initializeList(Generator<T> generator, int number) {
        return GeneratorAccessor.create(generator, number, new InitializationContext(), new PathNode.RootPathNode());
    }
}
