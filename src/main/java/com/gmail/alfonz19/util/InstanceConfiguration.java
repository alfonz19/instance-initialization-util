package com.gmail.alfonz19.util;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("squid:S119")//type variables
public class InstanceConfiguration<SOURCE_INSTANCE> {


    tady je potřeba udělat stejně root type a nested type + self bounding generics.


    private final Supplier<SOURCE_INSTANCE> instanceSupplier;

    public <ITEM_TYPE, COLLECTION_TYPE extends Collection<ITEM_TYPE>, GETTER_RETURN_TYPE>
    NestedCollectionConfiguration<ITEM_TYPE, COLLECTION_TYPE, InstanceConfiguration<SOURCE_INSTANCE>> initCollection(Class<ITEM_TYPE> itemType,
                                                                                                                     Supplier<COLLECTION_TYPE> collectionSupplier,
                                                                                                                     Function<SOURCE_INSTANCE, Collection<GETTER_RETURN_TYPE>> getterSelector) {
        return new NestedCollectionConfiguration<>(itemType, collectionSupplier, this);
    }

    public <K> InstanceConfiguration<SOURCE_INSTANCE> setValue(Function<SOURCE_INSTANCE, K> f, K value) {
        return this;
    }

    public <K extends Number> InstanceConfiguration<SOURCE_INSTANCE> initRandomlyFromRange(Function<SOURCE_INSTANCE, K> c, int i, int i1) {
        return this;
    }

    @SafeVarargs
    public final InstanceConfiguration<SOURCE_INSTANCE> initRandomly(Consumer<SOURCE_INSTANCE> ... c ) {  //replace with something like getter selector
        return this;
    }

    //------------------------

    public InstanceConfiguration(Supplier<SOURCE_INSTANCE> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    public SOURCE_INSTANCE create() {
        return instanceSupplier.get();
    }
}
