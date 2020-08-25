package com.gmail.alfonz19.util.attempt2;

import java.util.function.Supplier;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class CollectionConfiguration2<COLLECTION_TYPE> {
    public <ITEM_TYPE> CollectionItemsConfiguration<ITEM_TYPE> usingItemSupplier(Supplier<ITEM_TYPE> itemSupplier) {
        return new CollectionItemsConfiguration<>(itemSupplier);
    }
}
