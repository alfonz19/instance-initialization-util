package com.gmail.alfonz19.util.initialize.context.path;

import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import com.gmail.alfonz19.util.initialize.util.ReflectUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public interface PathNode {
    boolean isRoot();
    Object getCurrentValue(Object instance);
    void setValue(Object instance, Object value);
    CalculatedNodeData getCalculatedNodeData();
    void setCalculatedNodeData(CalculatedNodeData calculatedNodeData);
    Path getPath();
    PathNode getParent();

    abstract class AbstractPathNode implements PathNode {
        protected CalculatedNodeData calculatedNodeData;

        public AbstractPathNode() {
        }

        public AbstractPathNode(CalculatedNodeData calculatedNodeData) {
            this.calculatedNodeData = calculatedNodeData;
        }

        @Override
        public CalculatedNodeData getCalculatedNodeData() {
            return calculatedNodeData;
        }

        @Override
        public void setCalculatedNodeData(CalculatedNodeData calculatedNodeData) {
            this.calculatedNodeData = Objects.requireNonNull(calculatedNodeData);
        }
    }

    class RootPathNode extends AbstractPathNode {

        @Override
        public boolean isRoot() {
            return true;
        }

        @Override
        public PathNode getParent() {
            return null;
        }

        @Override
        public Object getCurrentValue(Object instance) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setValue(Object instance, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Path getPath() {
            return InstancePath.ROOT_PATH;
        }
    }

    abstract class AbstractNonRootNode extends AbstractPathNode {
        protected final Path path;
        protected final PathNode parent;

        protected AbstractNonRootNode(PathNode parent, Path path, CalculatedNodeData calculatedNodeData) {
            super(calculatedNodeData);
            this.parent = parent;
            this.path = path;
        }

        @Override
        public final Path getPath() {
            return path;
        }

        @Override
        public final boolean isRoot() {
            return false;
        }

        @Override
        public PathNode getParent() {
            return parent;
        }
    }

    class PropertyDescriptorBasedPathNode extends AbstractNonRootNode {

        private final PropertyDescriptor propertyDescriptor;
        private final Class<?> declaringClass;

        public PropertyDescriptorBasedPathNode(PathNode parent, PropertyDescriptor propertyDescriptor) {
            super(parent,
                    parent.getPath().createSubPathTraversingProperty(propertyDescriptor),
                    new CalculatedNodeData(propertyDescriptor.getPropertyType(),
                            ReflectUtil.substituteTypeVariables(propertyDescriptor, parent.getCalculatedNodeData().getTypeVariableAssignment()),
                            ReflectUtil.recalculateTypeVariableAssignment(propertyDescriptor, parent.getCalculatedNodeData().getTypeVariableAssignment())));
            this.propertyDescriptor = propertyDescriptor;
            this.declaringClass = propertyDescriptor.getReadMethod().getDeclaringClass();

        }

        @Override
        public Object getCurrentValue(Object instance) {
            try {
                return propertyDescriptor.getReadMethod().invoke(instance);
            } catch (IllegalAccessException| InvocationTargetException e) {
                throw new InitializeException("Unable to use PropertyDescriptor read method", e);
            }
        }

        @Override
        public void setValue(Object instance, Object value) {
            try {
                propertyDescriptor.getWriteMethod().invoke(instance, value);
            } catch (IllegalAccessException| InvocationTargetException|IllegalArgumentException e) {
                throw new InitializeException("Unable to use PropertyDescriptor write method", e);
            }
        }

        public Class<?> getDeclaringClass() {
            return declaringClass;
        }
    }

    class CollectionItemNode extends AbstractNonRootNode {

        public CollectionItemNode(PathNode parent, int index, CalculatedNodeData calculatedNodeData) {
            super(parent, parent.getPath().createSubPathTraversingArray(index), calculatedNodeData);
        }

        @Override
        public Object getCurrentValue(Object instance) {
            //TODO MMUCHA: implement — this is theoretically possible. If parent is already created, we might access this via index. if it's list or iterator based structure
            // but in general it is not a good idea to rely on this.
            throw new UnsupportedOperationException("Not implemented yet");
        }

        @Override
        public void setValue(Object instance, Object value) {
            throw new UnsupportedOperationException();
        }
    }
}
