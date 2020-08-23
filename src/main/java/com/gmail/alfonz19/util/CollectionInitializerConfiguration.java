package com.gmail.alfonz19.util;

import java.util.function.Function;
import java.util.function.Supplier;

public class CollectionInitializerConfiguration<PARENT_BUILDER, ITEM_TYPE> extends BuilderWithParentBuilderReference<PARENT_BUILDER>{


    public CollectionInitializerConfiguration(PARENT_BUILDER parentBuilder) {
        super(parentBuilder);
    }

//        public PARENT_BUILDER andThen() {
//            return getParentBuilder();
//        }

    public PARENT_BUILDER toSize(int i) {
        return getParentBuilder();
    }

    public PARENT_BUILDER toSize(int min, int max) {
        return getParentBuilder();
    }

    public PARENT_BUILDER asEmpty(int min, int max) {
        return getParentBuilder();
    }

    public PARENT_BUILDER asNull(int min, int max) {
        return getParentBuilder();
    }


    public CollectionInitializerConfiguration<PARENT_BUILDER, ITEM_TYPE> usingItemSupplier(Function<Integer, ITEM_TYPE> indexToNewItem) {
        return null;
    }

    public CollectionInitializerConfiguration<PARENT_BUILDER, ITEM_TYPE> usingItemSupplier(Supplier<ITEM_TYPE> itemSupplier) {
        return null;
    }
}
