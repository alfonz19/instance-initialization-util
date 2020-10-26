package com.gmail.alfonz19.util.initialize.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gmail.alfonz19.util.initialize.builder.CollectionConfiguration;
import com.gmail.alfonz19.util.initialize.builder.InstanceConfiguration;
import com.gmail.alfonz19.util.initialize.context.PathNode;
import com.gmail.alfonz19.util.initialize.util.ReflectUtil;
import com.gmail.alfonz19.util.initialize.util.TypeReferenceUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Generators {

    public static RandomStringInstanceGenerator randomString() {
        return new RandomStringInstanceGenerator();
    }

    public static ConstantBasedStringInstanceGenerator constantString(String initialValue) {
        return new ConstantBasedStringInstanceGenerator(initialValue);
    }

    public static IntInstanceGenerator randomInt() {
        return new IntInstanceGenerator();
    }

    public static IntInstanceGenerator randomIntFromRange(int min, int max) {
        return new IntInstanceGenerator().biggerThan(min).smallerThan(max);
    }

    public static LongInstanceGenerator randomLong() {
        return new LongInstanceGenerator();
    }

    public static LongInstanceGenerator randomLongFromRange(long min, long max) {
        return new LongInstanceGenerator().biggerThan(min).smallerThan(max);
    }

    public static ShortInstanceGenerator randomShort() {
        return new ShortInstanceGenerator();
    }

    public static ShortInstanceGenerator randomShortFromRange(short min, short max) {
        return new ShortInstanceGenerator().biggerThan(min).smallerThan(max);
    }

    public static ByteInstanceGenerator randomByte(byte min, byte max) {
        return new ByteInstanceGenerator();
    }

    public static ByteInstanceGenerator randomByteFromRange(byte min, byte max) {
        return new ByteInstanceGenerator().biggerThan(min).smallerThan(max);
    }

    public static <T> EnumInstanceGenerator<T, T> enumeratedType(Class<T> classType) {
        return new EnumInstanceGenerator<>(classType, classType);
    }

    public static RandomValueGenerator randomForGuessedType(boolean useDefaultValueAsFallback, boolean reusingGuessedType) {
        return new RandomValueGenerator(useDefaultValueAsFallback, reusingGuessedType);
    }

    public static <T> DefaultValueGenerator<T> defaultValue() {
        return new DefaultValueGenerator<>();
    }

    @SafeVarargs
    public static <T> Generator<T> roundRobinGenerator(Generator<T> ... generators) {
        return new RoundRobinGenerator<>(Arrays.asList(generators));
    }

    @SafeVarargs
    public static <T> Generator<T> sequentialGenerator(Generator<? extends T> ... generators) {
        return new SequentialGenerator<>(Arrays.asList(generators));
    }

    @SafeVarargs
    public static <T> Generator<T> randomlySelectedGenerator(Generator<T> ... generators) {
        return new RandomlySelectedGenerator<>(Arrays.asList(generators));
    }

    public static <T> Generator<T> limitedGenerator(int numberOfItems, Generator<T> generator) {
        return new LimitedGenerator<>(numberOfItems, generator);
    }

    public static <T> ConstantGenerator<T> constantGenerator(T value) {
        return new ConstantGenerator<>(value);
    }

    public static <T> ConstantGenerator<T> nullGenerator() {
        return new ConstantGenerator<>(null);
    }

    public static <T> InstanceConfiguration<T> instance(Supplier<T> instanceSupplier, TypeReference<T> typeReference) {
        return new InstanceConfiguration<>(TypeReferenceUtil.getRawTypeClassType(typeReference), instanceSupplier, typeReference);
    }

    public static <T> InstanceConfiguration<T> instance(TypeReference<T> typeReference) {
        Class<T> rawTypeClassType = TypeReferenceUtil.getRawTypeClassType(typeReference);
        Supplier<T> supplier = ReflectUtil.supplierFromClass(rawTypeClassType);
        return instance(supplier, typeReference);
    }

    public static <T> InstanceConfiguration<T> instance(Supplier<T> instanceSupplier) {
        return instance(null, instanceSupplier);
    }

    public static <T> InstanceConfiguration<T> instance(Class<T> instanceClass) {
        if (instanceClass.isInterface()) {
            throw new IllegalArgumentException("Cannot create generator from interface");
        }
        return instance(instanceClass, ReflectUtil.supplierFromClass(instanceClass));
    }

    //an alternative to type Generators.<{TYPE}>instance({supplier})
    public static <T, K extends T> InstanceConfiguration<T> instance(Class<T>classType, Supplier<K> instanceSupplier) {
        return new InstanceConfiguration<>(classType, instanceSupplier);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, List<ITEM_TYPE>> list(Class<ITEM_TYPE>clazz, Generator<ITEM_TYPE> itemGenerator) {
        return list(ArrayList::new, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, List<ITEM_TYPE>> list(Generator<? extends ITEM_TYPE> itemGenerator) {
        return list(ArrayList::new, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, List<ITEM_TYPE>> listWithNullItems(Class<ITEM_TYPE> itemClassType) {
        return list(ArrayList::new, nullGenerator());
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, List<ITEM_TYPE>> list(Function<Collection<? extends ITEM_TYPE>, List<ITEM_TYPE>> listInstanceCreationFunction,
                                                                                       Generator<? extends ITEM_TYPE> itemGenerator) {
        return new CollectionConfiguration<>(List.class, listInstanceCreationFunction, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, List<ITEM_TYPE>> list(TypeReference<List<ITEM_TYPE>> typeReference) {
        return list(ArrayList::new, typeReference);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, List<ITEM_TYPE>> list(
            Function<Collection<? extends ITEM_TYPE>, List<ITEM_TYPE>> listInstanceCreationFunction,
            TypeReference<List<ITEM_TYPE>> typeReference) {

        return new CollectionConfiguration<>(listInstanceCreationFunction, typeReference);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, Set<ITEM_TYPE>> set(Class<ITEM_TYPE>clazz, Generator<ITEM_TYPE> itemGenerator) {
        return set(HashSet::new, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, Set<ITEM_TYPE>> set(Generator<ITEM_TYPE> itemGenerator) {
        return set(HashSet::new, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, Set<ITEM_TYPE>> setWithNullItems(Class<ITEM_TYPE> itemClassType) {
        return set(HashSet::new, nullGenerator());
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, Set<ITEM_TYPE>> set(Function<Collection<? extends ITEM_TYPE>, Set<ITEM_TYPE>> listInstanceCreationFunction,
                                                                                     Generator<ITEM_TYPE> itemGenerator) {
        return new CollectionConfiguration<>(Set.class, listInstanceCreationFunction, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, Stream<ITEM_TYPE>> stream(Class<ITEM_TYPE>clazz, Generator<ITEM_TYPE> itemGenerator) {
        return stream(c -> new ArrayList<ITEM_TYPE>(c).stream(), itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, Stream<ITEM_TYPE>> stream(Generator<ITEM_TYPE> itemGenerator) {
        return stream(c -> new ArrayList<ITEM_TYPE>(c).stream(), itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, Stream<ITEM_TYPE>> streamWithNullItems(Class<ITEM_TYPE> itemClassType) {
        return stream(c -> new ArrayList<ITEM_TYPE>(c).stream(), nullGenerator());
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, Stream<ITEM_TYPE>> stream(Function<Collection<? extends ITEM_TYPE>, Stream<ITEM_TYPE>> listInstanceCreationFunction,
                                                                                           Generator<ITEM_TYPE> itemGenerator) {
        return new CollectionConfiguration<>(Stream.class, listInstanceCreationFunction, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, ITEM_TYPE[]> array(Class<ITEM_TYPE> arrayType, Generator<ITEM_TYPE> itemGenerator) {
        return new CollectionConfiguration<>(arrayType, items -> ReflectUtil.createArray(arrayType, items), itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, ITEM_TYPE[]> array(Generator<ITEM_TYPE> itemGenerator) {
        //noinspection unchecked
        Class<ITEM_TYPE> arrayType = (Class<ITEM_TYPE>) GeneratorAccessor.getCalculatedNodeData(itemGenerator).getClassType();
        return array(arrayType, itemGenerator);
    }

    public static <T> Generator<T> generatorFromSupplier(Supplier<T> supplier) {
        return new AbstractGenerator<T>() {
            @Override
            protected T create(PathNode pathNode) {
                return supplier.get();
            }
        };
    }
}
