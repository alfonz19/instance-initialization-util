package com.gmail.alfonz19.util;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class InitializeConfiguration<T> {

    private final Supplier<T> instanceSupplier;


    public <K> CollectionInitializerConfiguration<InitializeConfiguration<T>, K> initCollection(Function<T, Collection<K>> getAssociatedClassList) {
        return new CollectionInitializerConfiguration<>(this);
    }

    public <K> InitializeConfiguration<T> setValue(Function<T, K> f, K value) {
        return null;
    }

    public <K extends Number> InitializeConfiguration<T> initRandomlyFromRange(Function<T, K> c, int i, int i1) {
        return null;
    }

    @SafeVarargs
    public final InitializeConfiguration<T> initRandomly(Consumer<T> ... c ) {  //replace with something like getter selector
        return this;
    }

    //------------------------

    public InitializeConfiguration(Supplier<T> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    public T create() {
        return instanceSupplier.get();
    }

    //---------------------

    public static <T> InitializeConfiguration<T> initializeToCreate(Supplier<T> instanceSupplier) {
        return new InitializeConfiguration<>(instanceSupplier);
    }

    public static <T> InitializeConfiguration<T> initializeToCreate(Class<T> instance) {
        return initializeToCreate(()->{
            try {
                return instance.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);//todo better exception
            }
        });
    }
}
