package com.gmail.alfonz19.util;

import java.util.function.Function;
import java.util.function.Supplier;

public class CollectionInitializerConfiguration<PARENT_BUILDER, ITEM_TYPE> {

    private final InitializeConfiguration<PARENT_BUILDER> parentBuilder;

    public CollectionInitializerConfiguration(InitializeConfiguration<PARENT_BUILDER> parentBuilder) {
        this.parentBuilder = parentBuilder;
    }

//        public InitializeConfiguration<PARENT_BUILDER> andThen() {
//            return parentBuilder;
//        }

    public InitializeConfiguration<PARENT_BUILDER> toSize(int i) {
        return parentBuilder;
    }

    public InitializeConfiguration<PARENT_BUILDER> toSize(int min, int max) {
        return parentBuilder;
    }

    public InitializeConfiguration<PARENT_BUILDER> asEmpty(int min, int max) {
        return parentBuilder;
    }

    public InitializeConfiguration<PARENT_BUILDER> asNull(int min, int max) {
        return parentBuilder;
    }


    public CollectionInitializerConfiguration<PARENT_BUILDER, ITEM_TYPE> usingItemSupplier(Function<Integer, ITEM_TYPE> indexToNewItem) {
        return null;
    }

    public CollectionInitializerConfiguration<PARENT_BUILDER, ITEM_TYPE> usingItemSupplier(Supplier<ITEM_TYPE> itemSupplier) {
        return null;
    }
}
