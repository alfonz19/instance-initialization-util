package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.Path;
import com.gmail.alfonz19.util.initialize.context.PathMatcher;
import com.gmail.alfonz19.util.initialize.context.PathMatcherBuilder;
import com.gmail.alfonz19.util.initialize.context.PathNode;
import com.gmail.alfonz19.util.initialize.context.Rule;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class RuleBuilder {

    private final RuleImpl rule;

    public RuleBuilder(RuleImpl rule) {
        this.rule = rule;
    }

    public RuleBuilder ifPathEqualTo(Path path) {
        rule.addTest((pathNode) -> pathNode.getPath().equals(path));
        return this;
    }

    public RuleBuilder ifPathMatches(PathMatcherBuilder pathMatcherBuilder) {
        return ifPathMatches(pathMatcherBuilder.build());
    }

    public RuleBuilder ifPathMatches(PathMatcher pathMatcher) {
        rule.addTest((pathNode) -> pathMatcher.matches(pathNode.getPath()));
        return this;
    }

    public RuleBuilder addTest(Predicate<PathNode> test) {
        rule.addTest(test);
        return this;
    }

    public RuleBuilder ifClassTypeIsEqualTo(Class<?> requestedClassType) {
        addTest(classTypeIsEqualTo(requestedClassType));
        return this;
    }

    public RuleBuilder ifClassTypeIsAssignableFrom(Class<?> requestedClassType) {
        addTest(classTypeIsAssignableFrom(requestedClassType));
        return this;
    }

    public Rule build() {
        return rule;
    }

    public static Predicate<PathNode> classTypeIsAssignableFrom(Class<?> requestedClassType) {
        return (pathNode) -> requestedClassType.isAssignableFrom(pathNode.getCalculatedNodeData().getClassType());
    }

    public static Predicate<PathNode> classTypeIsEqualTo(Class<?> requestedClassType) {
        return (pathNode) -> requestedClassType.equals(pathNode.getCalculatedNodeData().getClassType());
    }

    private static Boolean applyAndOperation(Stream<Predicate<PathNode>> predicatesStream, PathNode pathNode) {
        return predicatesStream.map(e -> e.test(pathNode)).filter(e -> !e).findFirst().orElse(true);
    }

    @SafeVarargs
    public static Predicate<PathNode> and(Predicate<PathNode> first, Predicate<PathNode> ... others) {
        Stream<Predicate<PathNode>> predicateStream = Stream.concat(Stream.of(first), Arrays.stream(others));
        return node ->  applyAndOperation(predicateStream, node);
    }

    @SafeVarargs
    public static Predicate<PathNode> or(Predicate<PathNode> first, Predicate<PathNode> ... others) {
        Stream<Predicate<PathNode>> predicateStream = Stream.concat(Stream.of(first), Arrays.stream(others));
        return node -> predicateStream
                .map(e->e.test(node))

                //keep only true values
                .filter(e-> e)
                .findFirst().orElse(false);
    }

    public static RuleBuilder applyGenerator(Generator<?> generator) {
        return new RuleBuilder(new RuleImpl(generator));
    }

    @AllArgsConstructor
    private static final class RuleImpl implements Rule {

        private final Generator<?> generator;
        private final List<Predicate<PathNode>> tests = new LinkedList<>();

        @Override
        public boolean applies(PathNode pathNode) {
            //apply all rules with AND evaluation: apply all, find first false resolution, return it or return false if there is not false resolution.
            return applyAndOperation(tests.stream(), pathNode);
        }

        @Override
        public Generator<?> getGenerator() {
            return generator;
        }

        public void addTest(Predicate<PathNode> test) {
            tests.add(test);
        }
    }
}
