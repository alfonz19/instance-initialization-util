package com.gmail.alfonz19.util.initialize.context;


import com.gmail.alfonz19.util.initialize.generator.Rules;
import lombok.Getter;
import lombok.Setter;

import java.beans.PropertyDescriptor;
import java.util.Iterator;
import java.util.Objects;

@SuppressWarnings({"unused"})//unused constructs.
public class PathContext {
    private final PathContext parent;
    private final Rules rules;

    @Getter
    private final Path path;

    @Getter
    private CalculatedNodeData calculatedNodeData;

    public PathContext() {
        this(Rules.NO_RULES);
    }

    public PathContext(Rules rules) {
        this(new InstancePath(), null, rules);
    }

    public PathContext(Path path, PathContext parentContext) {
        this(path, parentContext, Rules.NO_RULES);
    }

    public PathContext(Path path, PathContext parent, Rules rules) {
        this.parent = parent;
        this.rules = rules;
        this.path = path;
    }

    public PathContext createSubPathTraversingProperty(PropertyDescriptor propertyDescriptor) {
        return new PathContext(path.createSubPathTraversingProperty(propertyDescriptor), this);
    }

    public PathContext createSubPathTraversingArray(int index) {
        return new PathContext(path.createSubPathTraversingArray(index), this);
    }

    public PathContext createSubPathTraversingMap(String key) {
        return new PathContext(path.createSubPathTraversingMap(key), this);
    }

    public void setCalculatedNodeData(CalculatedNodeData calculatedNodeData) {
        if (this.calculatedNodeData != null) {
            throw new IllegalStateException("Overwriting calculatedNodeData is not allowed.");
        }
        this.calculatedNodeData = Objects.requireNonNull(calculatedNodeData);
    }

    public Iterator<Rule> getJoinedRulesIteratorAcrossPathContext() {
        return new HierarchyRulesIterator();
    }

    @Setter
    @Getter
    public static class CalculatedNodeData {
        private Class<?> instanceClassType;
        //TODO MMUCHA: missing known generics types.

        public CalculatedNodeData(Class<?> instanceClassType) {
            this.instanceClassType = instanceClassType;
        }
    }

    private class HierarchyRulesIterator implements Iterator<Rule> {
        private PathContext pathContext;
        private Iterator<Rule> iterator;

        public HierarchyRulesIterator() {
            pathContext = PathContext.this;
            iterator = getRulesIteratorFromCurrentPathContext();
        }

        public Iterator<Rule> getRulesIteratorFromCurrentPathContext() {
            return pathContext.rules.iterator();
        }

        @Override
        public boolean hasNext() {
            while(true) {
                boolean hasNext = iterator.hasNext();
                if (hasNext) {
                    return true;
                }

                pathContext = pathContext.parent;
                if (pathContext == null) {
                    return false;
                } else {
                    iterator = getRulesIteratorFromCurrentPathContext();
                }
            }
        }

        @Override
        public Rule next() {
            return iterator.next();
        }
    }
}
