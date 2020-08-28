package com.gmail.alfonz19.util.initialize.builder;

import lombok.AccessLevel;
import lombok.Getter;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public abstract class BuilderWithParentBuilderReference<PARENT_BUILDER> {
    @Getter(AccessLevel.PROTECTED)
    private final PARENT_BUILDER parentBuilder;

    protected BuilderWithParentBuilderReference(PARENT_BUILDER parentBuilder) {
        this.parentBuilder = parentBuilder;
    }
}
