package com.gmail.alfonz19.util;

import java.util.Collection;
import java.util.function.Supplier;

@SuppressWarnings("squid:S119")//type variables
public class NestedCollectionConfiguration<ITEM_TYPE, COLLECTION_TYPE extends Collection<ITEM_TYPE>, PARENT_BUILDER>
        extends CollectionConfiguration<ITEM_TYPE, COLLECTION_TYPE, NestedCollectionConfiguration<ITEM_TYPE, COLLECTION_TYPE, PARENT_BUILDER>>
        implements CollectionConfigurationFinisher<PARENT_BUILDER> {

    private final PARENT_BUILDER parentBuilder;

    public NestedCollectionConfiguration(Class<ITEM_TYPE> clazz,
                                         Supplier<COLLECTION_TYPE> collectionSupplier,
                                         PARENT_BUILDER parentBuilder) {
        super(clazz, collectionSupplier);
        this.parentBuilder = parentBuilder;
    }

    @Override
    protected NestedCollectionConfiguration<ITEM_TYPE, COLLECTION_TYPE, PARENT_BUILDER> getSelf() {
        return this;
    }

    @Override
    public PARENT_BUILDER toSize(int i) {
        return parentBuilder;
    }

    @Override
    public PARENT_BUILDER toSize(int min, int max) {
        return parentBuilder;
    }

    @Override
    public PARENT_BUILDER asEmpty(int min, int max) {
        return parentBuilder;
    }

    @Override
    public PARENT_BUILDER asNull(int min, int max) {
        return parentBuilder;
    }
}
