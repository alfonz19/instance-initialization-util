package com.gmail.alfonz19.util.initialize.rules;

import com.gmail.alfonz19.util.initialize.context.InitializationContext;
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
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FindFirstApplicableRule {
    private static final Logger RULES_DEBUGGING_LOGGER = org.slf4j.LoggerFactory.getLogger("RULES_DEBUGGING");

    public static Optional<Generator<?>> getGeneratorFromFirstApplicableRule(Object instance, InitializationContext initializationContext, PathNode pathNode) {
        Stream<Rule> streamOfRules = iteratorToStream(new HierarchyRulesIterator(initializationContext, pathNode));

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
        private final InitializationContext initializationContext;
        private PathNode pathNode;
        private Iterator<Rule> iterator;

        public HierarchyRulesIterator(InitializationContext initializationContext,
                                      PathNode pathNode) {
            this.initializationContext = initializationContext;
            this.pathNode = pathNode;
            iterator = getRulesIteratorForCurrentPathNode();
        }

        public Iterator<Rule> getRulesIteratorForCurrentPathNode() {
            Rules rules = this.initializationContext.getRulesForPath(pathNode.getPath());
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
