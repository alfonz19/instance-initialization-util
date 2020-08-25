package com.gmail.alfonz19.util.attempt2;

import com.gmail.alfonz19.util.NestedCollectionConfiguration;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public abstract class InstanceConfiguration<SOURCE_INSTANCE, SELF_TYPE extends InstanceConfiguration<SOURCE_INSTANCE, SELF_TYPE>> {

    protected final Supplier<SOURCE_INSTANCE> instanceSupplier;

    public InstanceConfiguration(Supplier<SOURCE_INSTANCE> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    protected abstract SELF_TYPE getSelf();

    public <ITEM_TYPE, COLLECTION_TYPE extends Collection<ITEM_TYPE>, GETTER_RETURN_TYPE>
    NestedCollectionConfiguration<ITEM_TYPE, COLLECTION_TYPE, InstanceConfiguration<SOURCE_INSTANCE, SELF_TYPE>> initCollection(Class<ITEM_TYPE> itemType,
                                                                                                                                Supplier<COLLECTION_TYPE> collectionSupplier,
                                                                                                                                Function<SOURCE_INSTANCE, Collection<GETTER_RETURN_TYPE>> getterSelector) {
        return new NestedCollectionConfiguration<>(itemType, collectionSupplier, getSelf());
    }

    public <K> SELF_TYPE setValue(Function<SOURCE_INSTANCE, K> f, K value) {
        return getSelf();
    }

    public <K extends Number> SELF_TYPE initRandomlyFromRange(Function<SOURCE_INSTANCE, K> c, int i, int i1) {
        return getSelf();
    }

    @SafeVarargs
    public final SELF_TYPE initRandomly(Consumer<SOURCE_INSTANCE> ... c ) {  //replace with something like getter selector
        return getSelf();
    }
}
