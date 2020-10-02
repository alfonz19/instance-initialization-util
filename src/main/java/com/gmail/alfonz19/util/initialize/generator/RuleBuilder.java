package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.Path;
import com.gmail.alfonz19.util.initialize.context.PathContext;
import com.gmail.alfonz19.util.initialize.context.PathMatcher;
import com.gmail.alfonz19.util.initialize.context.PathMatcherBuilder;
import com.gmail.alfonz19.util.initialize.context.Rule;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;

public class RuleBuilder {

    private final RuleImpl rule;

    public RuleBuilder(RuleImpl rule) {
        this.rule = rule;
    }

    public RuleBuilder ifPathEqualTo(Path path) {
        rule.addTest((pathContext, classType) -> pathContext.getPath().equals(path));
        return this;
    }

    public RuleBuilder ifPathMatches(PathMatcherBuilder pathMatcherBuilder) {
        return ifPathMatches(pathMatcherBuilder.build());
    }

    public RuleBuilder ifPathMatches(PathMatcher pathMatcher) {
        rule.addTest((pathContext, classType) -> pathMatcher.matches(pathContext.getPath()));
        return this;
    }

    public RuleBuilder ifPropertyClassTypeIsEqualTo(Class<?> requestedClassType) {
        rule.addTest((pathContext, classType) -> requestedClassType.equals(classType));
        return this;
    }

    public RuleBuilder ifPropertyClassTypeIsAssignableFrom(Class<?> requestedClassType) {
        rule.addTest((pathContext, classType) -> requestedClassType.isAssignableFrom(classType));
        return this;
    }

    public Rule build() {
        return rule;
    }

    public static RuleBuilder applyGenerator(AbstractGenerator<?> generator) {
        return new RuleBuilder(new RuleImpl(generator));
    }

    @AllArgsConstructor
    private static final class RuleImpl implements Rule {

        private final AbstractGenerator<?> generator;
        private final List<BiPredicate<PathContext, Class<?>>> tests = new LinkedList<>();

        @Override
        public boolean appliesForPathAndType(PathContext pathContext, Class<?> classType) {
            return tests.stream().map(e -> e.test(pathContext, classType)).filter(e -> !e).findFirst().orElse(true);
        }

        @Override
        public AbstractGenerator<?> getGenerator() {
            return generator;
        }

        public void addTest(BiPredicate<PathContext, Class<?>> test) {
            tests.add(test);
        }
    }
}
