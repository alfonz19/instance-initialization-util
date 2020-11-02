package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.Path;
import com.gmail.alfonz19.util.initialize.context.PathMatcher;
import com.gmail.alfonz19.util.initialize.context.PathMatcherBuilder;
import com.gmail.alfonz19.util.initialize.rules.RuleBuilder.RuleTest;
import com.gmail.alfonz19.util.initialize.util.ReflectUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathNodePredicates {

    public static RuleTest pathMatches(PathMatcher pathMatcher) {
        return new RuleTest(String.format("if matches path %s", pathMatcher.describe()),
                (instance, pathNode) -> pathMatcher.matches(pathNode.getPath()));
    }

    public static RuleTest pathMatches(PathMatcherBuilder pathMatcherBuilder) {
        return pathMatches(pathMatcherBuilder.build());
    }

    public static RuleTest classTypeIsAssignableFrom(Class<?> requestedClassType) {
        return new RuleTest(String.format("if classType is assignable from %s", requestedClassType),
                (instance, pathNode) -> requestedClassType.isAssignableFrom(
                        ReflectUtil.getRawType(pathNode.getCalculatedNodeData().getGenericClassType())));
    }

    public static RuleTest classTypeIsEqualTo(Class<?> requestedClassType) {
        return new RuleTest(String.format("if classType is equal to %s", requestedClassType),
                (instance, pathNode) -> requestedClassType.equals(
                        ReflectUtil.getRawType(pathNode.getCalculatedNodeData().getGenericClassType())));
    }

    public static RuleTest classPredicate(String testDescription, Predicate<Class<?>> classPredicate) {
        return new RuleTest(testDescription,
                (instance, pathNode) -> classPredicate.test(pathNode.getCalculatedNodeData().getClassType()));
    }

    public static RuleTest classPredicate(Predicate<Class<?>> classPredicate) {
        return classPredicate("if <custom class predicate>", classPredicate);
    }

    public static RuleTest typePredicate(String testDescription, Predicate<Type> classPredicate) {
        return new RuleTest(testDescription,
                (instance, pathNode) -> classPredicate.test(pathNode.getCalculatedNodeData().getGenericClassType()));
    }

    public static RuleTest typePredicate(Predicate<Type> typePredicate) {
        return typePredicate("if <custom type predicate>", typePredicate);
    }

    public static RuleTest pathIsEqual(Path path) {
        return new RuleTest(String.format("if path is equal to %s", path),
                (instance, pathNode) -> pathNode.getPath().equals(path));
    }

    public static RuleTest pathLengthIsEqual(int length) {
        return new RuleTest(String.format("if path length is equal to %d", length),
                (instance, pathNode) -> pathNode.getPath().getPathLength() == length);
    }

    public static RuleTest pathLengthIsLessThan(int length) {
        return new RuleTest(String.format("if path length is less than %d", length),
                (instance, pathNode) -> pathNode.getPath().getPathLength() < length);
    }
}
