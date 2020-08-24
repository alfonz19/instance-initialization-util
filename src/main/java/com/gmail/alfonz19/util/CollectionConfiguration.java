package com.gmail.alfonz19.util;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public abstract class CollectionConfiguration<ITEM_TYPE,
        COLLECTION_TYPE extends Collection<ITEM_TYPE>,
        SELF_TYPE extends CollectionConfiguration<ITEM_TYPE, COLLECTION_TYPE, SELF_TYPE>> {


    private final Supplier<COLLECTION_TYPE> collectionSupplier;
    private final Class<ITEM_TYPE> clazz;
    private Supplier<ITEM_TYPE> itemSupplier;

    public CollectionConfiguration(Class<ITEM_TYPE> clazz, Supplier<COLLECTION_TYPE> collectionSupplier) {
        this.clazz = clazz;
        this.collectionSupplier = collectionSupplier;
    }

    protected abstract SELF_TYPE getSelf();


    public SELF_TYPE usingItemSupplier(Function<Integer, ITEM_TYPE> itemSupplier) {
        ////TODO MMUCHA: how to get specific supplier for ith item?
        return getSelf();
    }

    public SELF_TYPE usingItemSupplier(Supplier<ITEM_TYPE> itemSupplier) {
        this.itemSupplier = itemSupplier;
        return getSelf();
    }

    public InstanceConfiguration<ITEM_TYPE, NestedInstanceConfiguration<ITEM_TYPE, SELF_TYPE>> withEachItem() {
        return new NestedInstanceConfiguration<>(itemSupplier, getSelf());
    }

    //-----------

}
