package com.gmail.alfonz19.util;

import lombok.Data;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class InitializeConfiguration<T> {

    private final Supplier<T> instanceSupplier;
    private final Random rnd = new Random();

    public static void main(String[] args) {

        ToInit initialized = InitializeConfiguration.initializeToCreate(ToInit::new)
                .initRandomly(ToInit::getInitRandomly, ToInit::getInitRandomly2)
                .initRandomlyFromRange(ToInit::getInitFromRange, 1, 5)
                .initRandomlyFromRange(ToInit::getInitFromRange, 1, 5)
//                .initRandomlyFromRange(ToInit::getSomeStringValue, 1, 5) invalid!
                .set(ToInit::getSomeStringValue, "abc")

                .initCollection(ToInit::getAssociatedClassList).usingItemSupplier(index->new AssociatedClass()).toSize(3)
                .initCollection(ToInit::getAssociatedClassList).usingItemSupplier(AssociatedClass::new).toSize(3, 10)

                .initCollection(ToInit::getDoubleList).usingItemSupplier((Supplier<List<AssociatedClass>>) LinkedList::new).toSize(5)

                .create();

        System.out.println(initialized);

    }

    private <K> CollectionInitializerConfiguration<T, K> initCollection(Function<T, Collection<K>> getAssociatedClassList) {
        return new CollectionInitializerConfiguration<>(this);
    }

    private <K> InitializeConfiguration<T> set(Function<T, K> f, K value) {
        return null;
    }

    private <K extends Number> InitializeConfiguration<T> initRandomlyFromRange(Function<T, K> c, int i, int i1) {
        return null;
    }

    @SafeVarargs
    private final InitializeConfiguration<T> initRandomly(Consumer<T> ... c ) {  //replace with something like getter selector
        return this;
    }

    //------------------------

    public InitializeConfiguration(Supplier<T> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    public T create() {
        return instanceSupplier.get();
    }

    public static class CollectionInitializerConfiguration<PARENT_BUILDER, ITEM_TYPE> {

        private final InitializeConfiguration<PARENT_BUILDER> parentBuilder;

        public CollectionInitializerConfiguration(InitializeConfiguration<PARENT_BUILDER> parentBuilder) {
            this.parentBuilder = parentBuilder;
        }

//        public InitializeConfiguration<PARENT_BUILDER> andThen() {
//            return parentBuilder;
//        }

        public InitializeConfiguration<PARENT_BUILDER> toSize(int i) {
            return parentBuilder;
        }

        public InitializeConfiguration<PARENT_BUILDER> toSize(int min, int max) {
            return parentBuilder;
        }

        public InitializeConfiguration<PARENT_BUILDER> asEmpty(int min, int max) {
            return parentBuilder;
        }

        public InitializeConfiguration<PARENT_BUILDER> asNull(int min, int max) {
            return parentBuilder;
        }


        public CollectionInitializerConfiguration<PARENT_BUILDER, ITEM_TYPE> usingItemSupplier(Function<Integer, ITEM_TYPE> indexToNewItem) {
            return null;
        }

        public CollectionInitializerConfiguration<PARENT_BUILDER, ITEM_TYPE> usingItemSupplier(Supplier<ITEM_TYPE> itemSupplier) {
            return null;
        }
    }

    //---------------------

    @Data
    public static class ToInit {
        int initWithConstant;
        int initRandomly;
        int initRandomly2;
        int initFromRange;
        String someStringValue;
        List<AssociatedClass> associatedClassList;
        List<List<AssociatedClass>> doubleList;
    }

    public static class AssociatedClass {
        int a;
        int b;
    }

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
