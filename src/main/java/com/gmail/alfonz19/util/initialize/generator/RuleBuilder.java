package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.Path;
import com.gmail.alfonz19.util.initialize.context.PathMatcher;
import com.gmail.alfonz19.util.initialize.context.PathMatcherBuilder;
import com.gmail.alfonz19.util.initialize.context.PathNode;
import com.gmail.alfonz19.util.initialize.context.Rule;
import com.gmail.alfonz19.util.initialize.util.PredicatesBooleanOperations;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.gmail.alfonz19.util.initialize.generator.PathNodePredicates.*;

public class RuleBuilder {

    private final RuleImpl rule;

    public RuleBuilder(RuleImpl rule) {
        this.rule = rule;
    }

    public RuleBuilder ifPathEqualTo(Path path) {
        rule.addTest(pathIsEqual(path));
        return this;
    }

    public RuleBuilder ifPathLengthIs(int length) {
        rule.addTest(PathNodePredicates.pathLengthIsEqual(length));
        return this;
    }

    public RuleBuilder ifPathLengthIsLessThan(int length) {
        rule.addTest(PathNodePredicates.pathLengthIsLessThan(length));
        return this;
    }

    public RuleBuilder ifPathMatches(PathMatcherBuilder pathMatcherBuilder) {
        rule.addTest(pathMatches(pathMatcherBuilder));
        return this;
    }

    public RuleBuilder ifPathMatches(PathMatcher pathMatcher) {
        rule.addTest(PathNodePredicates.pathMatches(pathMatcher));
        return this;
    }

    public RuleBuilder addTest(BiPredicate<Object, PathNode> test) {
        rule.addTest(test);
        return this;
    }

    public RuleBuilder ifClassTypeIsEqualTo(Class<?> requestedClassType) {
        addTest(classTypeIsEqualTo(requestedClassType));
        return this;
    }

    public RuleBuilder ifPathNode(BiPredicate<Object, PathNode> predicate) {
        addTest(predicate);
        return this;
    }

    public RuleBuilder ifClass(Predicate<Class<?>> predicate) {
        addTest(classPredicate(predicate));
        return this;
    }

    public RuleBuilder ifClassTypeIsAssignableFrom(Class<?> requestedClassType) {
        addTest(classTypeIsAssignableFrom(requestedClassType));
        return this;
    }

    public Rule build() {
        return rule;
    }

    public static RuleBuilder applyGenerator(Generator<?> generator) {
        return new RuleBuilder(new RuleImpl(generator));
    }

    @AllArgsConstructor
    private static final class RuleImpl implements Rule {

        private final Generator<?> generator;
        private final List<BiPredicate<Object, PathNode>> tests = new LinkedList<>();

        @Override
        public boolean applies(Object instance, PathNode pathNode) {
            //apply all rules with AND evaluation: apply all, find first false resolution, return it or return false if there is not false resolution.
            return PredicatesBooleanOperations.applyAndOperation(tests.stream(), instance, pathNode);
        }

        @Override
        public Generator<?> getGenerator() {
            return generator;
        }

        public void addTest(BiPredicate<Object, PathNode> test) {
            tests.add(test);
        }
    }
}
