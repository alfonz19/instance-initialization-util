package com.gmail.alfonz19.util.initialize.rules;

import com.gmail.alfonz19.util.initialize.context.Path;
import com.gmail.alfonz19.util.initialize.context.PathMatcher;
import com.gmail.alfonz19.util.initialize.context.PathMatcherBuilder;
import com.gmail.alfonz19.util.initialize.context.PathNode;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.PathNodePredicates;
import com.gmail.alfonz19.util.initialize.util.PredicatesBooleanOperations;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.slf4j.Logger;

import static com.gmail.alfonz19.util.initialize.generator.PathNodePredicates.classPredicate;
import static com.gmail.alfonz19.util.initialize.generator.PathNodePredicates.classTypeIsAssignableFrom;
import static com.gmail.alfonz19.util.initialize.generator.PathNodePredicates.classTypeIsEqualTo;
import static com.gmail.alfonz19.util.initialize.generator.PathNodePredicates.pathIsEqual;
import static com.gmail.alfonz19.util.initialize.generator.PathNodePredicates.pathMatches;
import static com.gmail.alfonz19.util.initialize.generator.PathNodePredicates.typePredicate;

public class RuleBuilder {

    private final AbstractRule rule;

    public RuleBuilder(AbstractRule rule) {
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

    public RuleBuilder addTest(RuleTest test) {
        rule.addTest(test);
        return this;
    }

    public RuleBuilder ifClassTypeIsEqualTo(Class<?> requestedClassType) {
        addTest(classTypeIsEqualTo(requestedClassType));
        return this;
    }

    public RuleBuilder ifPathNode(String testDescription, BiPredicate<Object, PathNode> predicate) {
        addTest(new RuleTest(testDescription, predicate));
        return this;
    }

    public RuleBuilder ifPathNode(BiPredicate<Object, PathNode> predicate) {
        addTest(new RuleTest(predicate));
        return this;
    }

    public RuleBuilder ifClass(Predicate<Class<?>> predicate) {
        addTest(classPredicate(predicate));
        return this;
    }

    public RuleBuilder ifType(String testDescription, Predicate<Type> predicate) {
        addTest(typePredicate(testDescription, predicate));
        return this;
    }

    public RuleBuilder ifType(Predicate<Type> predicate) {
        addTest(typePredicate(predicate));
        return this;
    }

    public RuleBuilder ifClassTypeIsAssignableFrom(Class<?> requestedClassType) {
        addTest(classTypeIsAssignableFrom(requestedClassType));
        return this;
    }

    public RuleBuilder toCreateRule(String description) {
        rule.setRuleDescription(description);
        return this;
    }

    public Rule build() {
        return rule;
    }

    public static RuleBuilder createNewGeneratorAndApply(Supplier<Generator<?>> generator) {
        return new RuleBuilder(new SupplierRule(generator));
    }

    public static RuleBuilder applyGenerator(Generator<?> generator) {
        return new RuleBuilder(new RuleReturningConstant(generator));
    }

    @NoArgsConstructor
    public static abstract class AbstractRule implements Rule {
        private static final Logger RULES_DEBUGGING_LOGGER = org.slf4j.LoggerFactory.getLogger("RULES_DEBUGGING");
        private final List<RuleTest> tests = new LinkedList<>();
        private String ruleDescription = null;

        @Override
        public boolean applies(Object instance, PathNode pathNode) {
            if (RULES_DEBUGGING_LOGGER.isTraceEnabled()) {
                if (ruleDescription == null) {
                    this.ruleDescription = "<no rule description provided>, classType=" + this.getClass().getName();
                }
                String msgToLog = String.format(
                        "\t• Verifying rule '%s':\n\t\tEvaluating tests: %s\n",
                        ruleDescription,
                        PredicatesBooleanOperations.rulesDescriptionJoinedUsingPrefixNotation(tests.stream(), "and"));
                Arrays.stream(msgToLog.split("\n")).forEach(RULES_DEBUGGING_LOGGER::trace);

            }
            //apply all rules with AND evaluation: apply all, find first false resolution, return it or return false if there is not false resolution.
            Boolean result = PredicatesBooleanOperations.applyAndOperation(tests.stream(), instance, pathNode);
            RULES_DEBUGGING_LOGGER.trace("\t\tRule evaluation: {}", result);
            RULES_DEBUGGING_LOGGER.trace("");
            return result;
        }

        public void addTest(RuleTest test) {
            tests.add(test);
        }

        public void addTest(String testDescription, BiPredicate<Object, PathNode> test) {
            tests.add(new RuleTest(testDescription, test));
        }

        @Override
        public String getRuleDescription() {
            return ruleDescription;
        }

        public void setRuleDescription(String ruleDescription) {
            this.ruleDescription = Objects.requireNonNull(ruleDescription);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class RuleTest {
        private final String description;
        private final BiPredicate<Object, PathNode> predicate;

        public RuleTest(BiPredicate<Object, PathNode> predicate) {
            this("<unexplained test>", predicate);
        }
    }

    @AllArgsConstructor
    private static final class RuleReturningConstant extends AbstractRule  {

        private final Generator<?> generator;

        @Override
        public Generator<?> getGenerator() {
            return generator;
        }
    }

    @AllArgsConstructor
    private static final class SupplierRule extends AbstractRule  {

        private final Supplier<Generator<?>> generatorSupplier;

        @Override
        public Generator<?> getGenerator() {
            return generatorSupplier.get();
        }
    }
}
