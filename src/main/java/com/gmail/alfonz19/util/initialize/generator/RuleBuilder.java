package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.Path;
import com.gmail.alfonz19.util.initialize.context.PathMatcher;
import com.gmail.alfonz19.util.initialize.context.PathMatcherBuilder;
import com.gmail.alfonz19.util.initialize.context.PathNode;
import com.gmail.alfonz19.util.initialize.context.Rule;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

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

    public RuleBuilder ifClassTypeIsEqualTo(Class<?> requestedClassType) {
        rule.addTest((pathNode) -> requestedClassType.equals(pathNode.getCalculatedNodeData().getClassType()));
        return this;
    }

    public RuleBuilder ifClassTypeIsAssignableFrom(Class<?> requestedClassType) {
        rule.addTest((pathNode) -> requestedClassType.isAssignableFrom(pathNode.getCalculatedNodeData().getClassType()));
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
        private final List<Predicate<PathNode>> tests = new LinkedList<>();

        @Override
        public boolean applies(PathNode pathNode) {
            return tests.stream().map(e -> e.test(pathNode)).filter(e -> !e).findFirst().orElse(true);
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
