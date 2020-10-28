package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.Path;
import com.gmail.alfonz19.util.initialize.context.PathMatcher;
import com.gmail.alfonz19.util.initialize.context.PathMatcherBuilder;
import com.gmail.alfonz19.util.initialize.context.PathNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathNodePredicates {

    public static Predicate<PathNode> pathMatches(PathMatcher pathMatcher) {
        return (pathNode) -> pathMatcher.matches(pathNode.getPath());
    }

    public static Predicate<PathNode> pathMatches(PathMatcherBuilder pathMatcherBuilder) {
        return pathMatches(pathMatcherBuilder.build());
    }

    public static Predicate<PathNode> classTypeIsAssignableFrom(Class<?> requestedClassType) {
        return (pathNode) -> requestedClassType.isAssignableFrom(pathNode.getCalculatedNodeData().getClassType());
    }

    public static Predicate<PathNode> classTypeIsEqualTo(Class<?> requestedClassType) {
        return (pathNode) -> requestedClassType.equals(pathNode.getCalculatedNodeData().getClassType());
    }

    public static Predicate<PathNode> pathIsEqual(Path path) {
        return (pathNode) -> pathNode.getPath().equals(path);
    }

    public static Predicate<PathNode> pathLengthIsEqual(int length) {
        return (pathNode) -> pathNode.getPath().getPathLength() == length;
    }

    public static Predicate<PathNode> pathLengthIsLessThan(int length) {
        return (pathNode) -> pathNode.getPath().getPathLength() < length;

    }
}
