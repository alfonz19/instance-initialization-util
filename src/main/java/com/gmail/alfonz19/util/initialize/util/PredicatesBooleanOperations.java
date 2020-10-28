package com.gmail.alfonz19.util.initialize.util;

import com.gmail.alfonz19.util.initialize.context.PathNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PredicatesBooleanOperations {

    public static Boolean applyAndOperation(Stream<Predicate<PathNode>> predicatesStream, PathNode pathNode) {
        return predicatesStream.map(e -> e.test(pathNode)).filter(e -> !e).findFirst().orElse(true);
    }

    @SafeVarargs
    public static Predicate<PathNode> and(Predicate<PathNode> first, Predicate<PathNode>... others) {
        Stream<Predicate<PathNode>> predicateStream = Stream.concat(Stream.of(first), Arrays.stream(others));
        return node -> applyAndOperation(predicateStream, node);
    }

    @SafeVarargs
    public static Predicate<PathNode> or(Predicate<PathNode> first, Predicate<PathNode>... others) {
        Stream<Predicate<PathNode>> predicateStream = Stream.concat(Stream.of(first), Arrays.stream(others));
        return node -> predicateStream
                .map(e -> e.test(node))

                //keep only true values
                .filter(e -> e)
                .findFirst().orElse(false);
    }
}
