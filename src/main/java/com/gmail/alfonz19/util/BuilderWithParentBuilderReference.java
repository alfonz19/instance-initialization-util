package com.gmail.alfonz19.util;

public abstract class BuilderWithParentBuilderReference<PARENT_BUILDER> {
    private PARENT_BUILDER parentBuilder;

    public PARENT_BUILDER getParentBuilder() {//TODO MMUCHA: lombok.
        return parentBuilder;
    }

    protected BuilderWithParentBuilderReference(PARENT_BUILDER parentBuilder) {
        this.parentBuilder = parentBuilder;
    }
}
