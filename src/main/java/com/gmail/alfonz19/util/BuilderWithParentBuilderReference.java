package com.gmail.alfonz19.util;

import lombok.Getter;

public abstract class BuilderWithParentBuilderReference<PARENT_BUILDER> {
    @Getter
    private PARENT_BUILDER parentBuilder;

    protected BuilderWithParentBuilderReference(PARENT_BUILDER parentBuilder) {
        this.parentBuilder = parentBuilder;
    }
}
