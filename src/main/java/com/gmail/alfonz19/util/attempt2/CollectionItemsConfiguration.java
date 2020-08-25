package com.gmail.alfonz19.util.attempt2;

import com.gmail.alfonz19.util.NestedInstanceConfiguration;

import java.util.function.Supplier;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class CollectionItemsConfiguration<ITEM_TYPE> {

    private final Supplier<ITEM_TYPE> itemSupplier;

    public CollectionItemsConfiguration(Supplier<ITEM_TYPE> itemSupplier) {
        this.itemSupplier = itemSupplier;
    }

    public NestedInstanceConfiguration<ITEM_TYPE, CollectionItemsConfiguration<ITEM_TYPE>> withEachItem() {
        return new NestedInstanceConfiguration<>(null, this);
    }
}
