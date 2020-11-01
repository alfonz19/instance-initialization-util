package com.gmail.alfonz19.util.initialize.util;

import com.gmail.alfonz19.util.initialize.context.PathNode;
import com.gmail.alfonz19.util.initialize.generator.RuleBuilder;
import com.gmail.alfonz19.util.initialize.generator.RuleBuilder.RuleTest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PredicatesBooleanOperations {

    private static final Logger RULES_DEBUGGING_LOGGER = org.slf4j.LoggerFactory.getLogger("RULES_DEBUGGING");

    public static Boolean applyAndOperation(Stream<RuleTest> predicatesStream, Object instance, PathNode pathNode) {
        return predicatesStream
                .map(e -> {
                    boolean testResult = e.getPredicate().test(instance, pathNode);
                    logTestResult(e, testResult);
                    return testResult;
                })
                .filter(e -> !e).findFirst()
                .orElse(true);
    }

    public static RuleTest and(RuleTest first, RuleTest... others) {
        Stream<RuleTest> str = Stream.concat(Stream.of(first), Arrays.stream(others));
        String description = rulesDescriptionJoinedUsingPrefixNotation(str, "and");

        return new RuleTest(description,
                (instance, node) -> applyAndOperation(
                        Stream.concat(Stream.of(first), Arrays.stream(others)),
                        instance,
                        node));
    }

    public static String rulesDescriptionJoinedUsingPrefixNotation(Stream<RuleTest> testsStream, String operation) {
        return String.format("%s(%s)", operation,
                testsStream.map(RuleTest::getDescription).collect(Collectors.joining(", ")));
    }

    public static RuleTest or(RuleTest first, RuleTest... others) {
        String description =
                rulesDescriptionJoinedUsingPrefixNotation(Stream.concat(Stream.of(first), Arrays.stream(others)), "or");

        return new RuleTest(description,
                (instance, node) -> Stream.concat(Stream.of(first), Arrays.stream(others))
                        .map(e -> {
                            boolean testResult = e.getPredicate().test(instance, node);
                            logTestResult(e, testResult);
                            return testResult;
                        })

                        //keep only true values
                        .filter(e -> e)
                        .findFirst().orElse(false));
    }

    private static void logTestResult(RuleTest e, boolean testResult) {
        RULES_DEBUGGING_LOGGER.trace("\t• Test '{}' evaluated as {}", e.getDescription(), testResult);
    }
}
