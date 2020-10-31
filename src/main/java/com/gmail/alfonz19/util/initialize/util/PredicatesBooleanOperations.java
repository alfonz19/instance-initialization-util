package com.gmail.alfonz19.util.initialize.util;

import com.gmail.alfonz19.util.initialize.context.PathNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PredicatesBooleanOperations {

    public static Boolean applyAndOperation(Stream<BiPredicate<Object, PathNode>> predicatesStream, Object instance, PathNode pathNode) {
        return predicatesStream.map(e -> e.test(instance, pathNode)).filter(e -> !e).findFirst().orElse(true);
    }

    @SafeVarargs
    public static BiPredicate<Object, PathNode> and(BiPredicate<Object, PathNode> first, BiPredicate<Object, PathNode>... others) {
        Stream<BiPredicate<Object, PathNode>> predicateStream = Stream.concat(Stream.of(first), Arrays.stream(others));
        return (instance, node) -> applyAndOperation(predicateStream, instance, node);
    }

    @SafeVarargs
    public static BiPredicate<Object, PathNode> or(BiPredicate<Object, PathNode> first, BiPredicate<Object, PathNode>... others) {
        Stream<BiPredicate<Object, PathNode>> predicateStream = Stream.concat(Stream.of(first), Arrays.stream(others));
        return (instance, node) -> predicateStream
                .map(e -> e.test(instance, node))

                //keep only true values
                .filter(e -> e)
                .findFirst().orElse(false);
    }
}
