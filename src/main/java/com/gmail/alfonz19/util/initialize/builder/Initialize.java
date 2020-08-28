package com.gmail.alfonz19.util.initialize.builder;

import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import com.gmail.alfonz19.util.initialize.util.ReflectUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Initialize {

    public static <T> Generator<T> nullValue() {
        return Generators.nullGenerator();
    }

    public static <T> InstanceConfiguration<T> instance(Supplier<T> instanceSupplier) {
        return new InstanceConfiguration<>(instanceSupplier);
    }

    public static <T> InstanceConfiguration<T> instance(Class<T> instance) {
        return instance(ReflectUtil.supplierFromClass(instance));
    }

    //an alternative to type Initialize.<{TYPE}>instance({supplier})
    public static <T, K extends T> InstanceConfiguration<T> instance(Class<T>clazz, Supplier<K> instanceSupplier) {
        return new InstanceConfiguration<>(instanceSupplier);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, List<ITEM_TYPE>> list(Class<ITEM_TYPE>clazz, Generator<ITEM_TYPE> itemGenerator) {
        return list(ArrayList::new, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, List<ITEM_TYPE>> list(Generator<? extends ITEM_TYPE> itemGenerator) {
        return list(ArrayList::new, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, List<ITEM_TYPE>> listWithNullItems(Class<ITEM_TYPE> itemClassType) {
        return list(ArrayList::new, Generators.nullGenerator());
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, List<ITEM_TYPE>> list(Function<Collection<? extends ITEM_TYPE>, List<ITEM_TYPE>> listInstanceCreationFunction,
                                                                                       Generator<? extends ITEM_TYPE> itemGenerator) {
        return new CollectionConfiguration<>(listInstanceCreationFunction, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, Set<ITEM_TYPE>> set(Class<ITEM_TYPE>clazz, Generator<ITEM_TYPE> itemGenerator) {
        return set(HashSet::new, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, Set<ITEM_TYPE>> set(Generator<ITEM_TYPE> itemGenerator) {
        return set(HashSet::new, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, Set<ITEM_TYPE>> setWithNullItems(Class<ITEM_TYPE> itemClassType) {
        return set(HashSet::new, Generators.nullGenerator());
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, Set<ITEM_TYPE>> set(Function<Collection<? extends ITEM_TYPE>, Set<ITEM_TYPE>> listInstanceCreationFunction,
                                                                                     Generator<ITEM_TYPE> itemGenerator) {
        return new CollectionConfiguration<>(listInstanceCreationFunction, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, Stream<ITEM_TYPE>> stream(Class<ITEM_TYPE>clazz, Generator<ITEM_TYPE> itemGenerator) {
        return stream(c -> new ArrayList<ITEM_TYPE>(c).stream(), itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, Stream<ITEM_TYPE>> stream(Generator<ITEM_TYPE> itemGenerator) {
        return stream(c -> new ArrayList<ITEM_TYPE>(c).stream(), itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, Stream<ITEM_TYPE>> streamWithNullItems(Class<ITEM_TYPE> itemClassType) {
        return stream(c -> new ArrayList<ITEM_TYPE>(c).stream(), Generators.nullGenerator());
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, Stream<ITEM_TYPE>> stream(Function<Collection<? extends ITEM_TYPE>, Stream<ITEM_TYPE>> listInstanceCreationFunction,
                                                                                     Generator<ITEM_TYPE> itemGenerator) {
        return new CollectionConfiguration<>(listInstanceCreationFunction, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, ITEM_TYPE[]> array(Class<ITEM_TYPE> arrayType, Generator<ITEM_TYPE> itemGenerator) {
        return array(items -> ReflectUtil.createArray(arrayType, items), itemGenerator);
    }

    private static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, ITEM_TYPE[]> array(Function<Collection<? extends ITEM_TYPE>, ITEM_TYPE[]> arrayInstanceCreationFunction, Generator<ITEM_TYPE> itemGenerator) {
        return new CollectionConfiguration<>(arrayInstanceCreationFunction, itemGenerator);
    }
}
