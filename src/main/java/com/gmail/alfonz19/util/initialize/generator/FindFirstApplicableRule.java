package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.PathNode;
import com.gmail.alfonz19.util.initialize.context.Rule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FindFirstApplicableRule {
    public static Optional<Generator<?>> getGeneratorFromFirstApplicableRule(List<Rule> rules, Object instance, PathNode pathNode) {
        return rules.stream()
                .filter(rule->rule.applies(instance, pathNode))
                .findFirst()
                .map(Rule::getGenerator);
    }
}
