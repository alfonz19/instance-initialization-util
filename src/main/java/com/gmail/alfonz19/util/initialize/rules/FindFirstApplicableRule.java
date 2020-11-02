package com.gmail.alfonz19.util.initialize.rules;

import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FindFirstApplicableRule {
    private static final Logger RULES_DEBUGGING_LOGGER = org.slf4j.LoggerFactory.getLogger("RULES_DEBUGGING");

    public static Optional<Generator<?>> getGeneratorFromFirstApplicableRule(List<Rule> rules, Object instance, PathNode pathNode) {
        String msgToLog = String.format(
                "Looking for generator for path='%s':\n\t↳ classType='%s'\n\t↳ genericClassType='%s'\n",
                pathNode.getPath(),
                pathNode.getCalculatedNodeData().getClassType(),
                pathNode.getCalculatedNodeData().getGenericClassType());
        Arrays.stream(msgToLog.split("\n", -1)).forEach(RULES_DEBUGGING_LOGGER::trace);

        Optional<Generator<?>> result = rules.stream()
                .filter(rule -> rule.applies(instance, pathNode))
                .findFirst()
                .map(Rule::getGenerator);

        RULES_DEBUGGING_LOGGER.trace("Generator for path='{}' {}.", pathNode.getPath(), result.isPresent() ? "found" : "not found");
        RULES_DEBUGGING_LOGGER.trace("");
        RULES_DEBUGGING_LOGGER.trace("");

        return result;
    }
}
