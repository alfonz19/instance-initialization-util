package com.gmail.alfonz19.util.initialize.rules;

import com.gmail.alfonz19.util.initialize.context.InitializationConfiguration;
import com.gmail.alfonz19.util.initialize.context.path.Path;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.generator.Generator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FindFirstApplicableRule {
    private static final Logger RULES_DEBUGGING_LOGGER = org.slf4j.LoggerFactory.getLogger("RULES_DEBUGGING");

    public static Optional<Generator<?>> getGeneratorFromFirstApplicableRule(InitializationConfiguration initializationConfiguration, PathNode pathNode) {
        return getGeneratorFromFirstApplicableRule(null, initializationConfiguration, pathNode);
    }

    public static Optional<Generator<?>> getGeneratorFromFirstApplicableRule(Object instance, InitializationConfiguration initializationConfiguration, PathNode pathNode) {
        return getGeneratorFromFirstApplicableRule(instance, pathNode, initializationConfiguration::getRulesForPath);
    }

    public static Optional<Generator<?>> getGeneratorFromFirstApplicableRandomGeneratorRule(Object instance, InitializationConfiguration initializationConfiguration, PathNode pathNode) {
        return getGeneratorFromFirstApplicableRule(instance, pathNode, initializationConfiguration::getRandomValueGeneratorRulesForPath);
    }

    private static Optional<Generator<?>> getGeneratorFromFirstApplicableRule(Object instance,
                                                                             PathNode pathNode,
                                                                             Function<Path, Rules> getRulesForPath) {
        Stream<Rule> streamOfRules = iteratorToStream(new HierarchyRulesIterator(pathNode,
                getRulesForPath));

        String msgToLog = String.format(
                "Looking for generator for path='%s':\n\t↳ classType='%s'\n\t↳ genericClassType='%s'\n",
                pathNode.getPath(),
                pathNode.getCalculatedNodeData().getClassType(),
                pathNode.getCalculatedNodeData().getGenericClassType());
        Arrays.stream(msgToLog.split("\n", -1)).forEach(RULES_DEBUGGING_LOGGER::trace);

        Optional<Generator<?>> result = streamOfRules
                .filter(rule -> rule.applies(instance, pathNode))
                .findFirst()
                .map(Rule::getGenerator);

        RULES_DEBUGGING_LOGGER.trace("Generator for path='{}' {}.", pathNode.getPath(), result.isPresent() ? "found" : "not found");
        RULES_DEBUGGING_LOGGER.trace("");
        RULES_DEBUGGING_LOGGER.trace("");

        return result;
    }

    private static Stream<Rule> iteratorToStream(Iterator<Rule> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

    private static class HierarchyRulesIterator implements Iterator<Rule> {
        private final Function<Path, Rules> getRulesForPath;
        private PathNode pathNode;
        private Iterator<Rule> iterator;


        public HierarchyRulesIterator(PathNode pathNode, Function<Path, Rules> getRulesForPath) {
            this.pathNode = pathNode;
            this.getRulesForPath = getRulesForPath;
            this.iterator = getRulesIteratorForCurrentPathNode();
        }

        public Iterator<Rule> getRulesIteratorForCurrentPathNode() {
            Rules rules = getRulesForPath.apply(pathNode.getPath());
            return rules == null ? Collections.emptyListIterator() : rules.iterator();
        }

        @Override
        public boolean hasNext() {
            while(true) {
                boolean hasNext = iterator.hasNext();
                if (hasNext) {
                    return true;
                }

                pathNode = pathNode.getParent();
                if (pathNode == null) {
                    return false;
                } else {
                    iterator = getRulesIteratorForCurrentPathNode();
                }
            }
        }

        @Override
        public Rule next() {
            return iterator.next();
        }
    }
}
