package com.gmail.alfonz19.util;

import java.util.Collection;
import java.util.function.Supplier;

@SuppressWarnings("squid:S119")//type variables
public class RootCollectionConfiguration<ITEM_TYPE, COLLECTION_TYPE extends Collection<ITEM_TYPE>>
        extends CollectionConfiguration<ITEM_TYPE, COLLECTION_TYPE, RootCollectionConfiguration<ITEM_TYPE, COLLECTION_TYPE>>
        implements CollectionConfigurationFinisher<COLLECTION_TYPE> {

    public RootCollectionConfiguration(Class<ITEM_TYPE> clazz, Supplier<COLLECTION_TYPE> collectionSupplier) {
        super(clazz, collectionSupplier);
    }

    @Override
    protected RootCollectionConfiguration<ITEM_TYPE, COLLECTION_TYPE> getSelf() {
        return this;
    }

    @Override
    public COLLECTION_TYPE toSize(int i) {
        return null;
    }

    @Override
    public COLLECTION_TYPE toSize(int min, int max) {
        return null;
    }

    @Override
    public COLLECTION_TYPE asEmpty(int min, int max) {
        return null;
    }

    @Override
    public COLLECTION_TYPE asNull(int min, int max) {
        return null;
    }
}
