package com.gmail.alfonz19.util;

import com.gmail.alfonz19.util.to.testing.ToInit;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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

    public <K extends Number> SELF_TYPE initRandomlyFromRange(Function<SOURCE_INSTANCE, K> c, int i, int i1, BiFunction<PathContext, K, K> contextModifier) {
        return getSelf();
    }

    public SELF_TYPE initRandomly(Function<SOURCE_INSTANCE, String> c) {
        return getSelf();
    }

    public SELF_TYPE initRandomly(Function<SOURCE_INSTANCE, String> stringFieldSelector, BiFunction<PathContext, String, String> contextModifier) {
        return getSelf();
    }

    @SafeVarargs
    public final SELF_TYPE initRandomly(Consumer<SOURCE_INSTANCE> ... c ) {  //replace with something like getter selector
        return getSelf();
    }

    public final <K> SELF_TYPE referingToFieldUpContextPath(Function<SOURCE_INSTANCE, K> stringFieldSelector, int levelsUp) {
        return getSelf();
    }

    public final <K, L> SELF_TYPE referingToFieldUpContextPath(Function<SOURCE_INSTANCE, K> stringFieldSelector, int levelsUp, Class<L> expectedTypeOfUpNode, Function<L, K> selector) {
        return getSelf();
    }

    public final SELF_TYPE withPathContext(BiConsumer<SELF_TYPE, PathContext> withPathContext) {
        withPathContext.accept(getSelf(), new PathContext());
        return getSelf();
    }

    public static class PathContext {
        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
