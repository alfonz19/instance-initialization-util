package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.Path;
import com.gmail.alfonz19.util.initialize.context.PathMatcher;
import com.gmail.alfonz19.util.initialize.context.PathMatcherBuilder;
import com.gmail.alfonz19.util.initialize.context.PathNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathNodePredicates {

    public static BiPredicate<Object, PathNode> pathMatches(PathMatcher pathMatcher) {
        return (instance, pathNode) -> pathMatcher.matches(pathNode.getPath());
    }

    public static BiPredicate<Object, PathNode> pathMatches(PathMatcherBuilder pathMatcherBuilder) {
        return pathMatches(pathMatcherBuilder.build());
    }

    public static BiPredicate<Object, PathNode> classTypeIsAssignableFrom(Class<?> requestedClassType) {
        return (instance, pathNode) -> requestedClassType.isAssignableFrom(pathNode.getCalculatedNodeData().getClassType());
    }

    public static BiPredicate<Object, PathNode> classTypeIsEqualTo(Class<?> requestedClassType) {
        return (instance, pathNode) -> requestedClassType.equals(pathNode.getCalculatedNodeData().getClassType());
    }

    public static BiPredicate<Object, PathNode> classPredicate(Predicate<Class<?>> classPredicate) {
        return (instance, pathNode) -> classPredicate.test(pathNode.getCalculatedNodeData().getClassType());
    }

    public static BiPredicate<Object, PathNode> pathIsEqual(Path path) {
        return (instance, pathNode) -> pathNode.getPath().equals(path);
    }

    public static BiPredicate<Object, PathNode> pathLengthIsEqual(int length) {
        return (instance, pathNode) -> pathNode.getPath().getPathLength() == length;
    }

    public static BiPredicate<Object, PathNode> pathLengthIsLessThan(int length) {
        return (instance, pathNode) -> pathNode.getPath().getPathLength() < length;
    }
}
