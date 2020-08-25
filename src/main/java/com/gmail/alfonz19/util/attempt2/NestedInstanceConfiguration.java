package com.gmail.alfonz19.util.attempt2;

import java.util.function.Supplier;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class NestedInstanceConfiguration<SOURCE_INSTANCE, PARENT_BUILDER> extends
        InstanceConfiguration<SOURCE_INSTANCE, NestedInstanceConfiguration<SOURCE_INSTANCE, PARENT_BUILDER>> {


    private final PARENT_BUILDER parentBuilder;

    public NestedInstanceConfiguration(Supplier<SOURCE_INSTANCE> sourceInstanceSupplier, PARENT_BUILDER parentBuilder) {
        super(sourceInstanceSupplier);
        this.parentBuilder = parentBuilder;
    }

    @Override
    protected NestedInstanceConfiguration<SOURCE_INSTANCE, PARENT_BUILDER> getSelf() {
        return null;
    }

    public PARENT_BUILDER create() {
        return parentBuilder;
    }

    public PARENT_BUILDER beingNull() {
        return parentBuilder;
    }
}
