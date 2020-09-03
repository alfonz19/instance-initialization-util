package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.builder.CollectionConfiguration;
import com.gmail.alfonz19.util.initialize.builder.InstanceConfiguration;
import com.gmail.alfonz19.util.initialize.util.ReflectUtil;
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

    public static <T> Generator<T> constantGenerator(T value) {
        return path -> value;
    }

    public static <T> Generator<T> nullGenerator() {
        return path -> null;
    }

    public static <T> InstanceConfiguration<T> instance(Supplier<T> instanceSupplier) {
        return new InstanceConfiguration<>(instanceSupplier);
    }

    public static <T> InstanceConfiguration<T> instance(Class<T> instance) {
        return instance(ReflectUtil.supplierFromClass(instance));
    }

    //an alternative to type Generators.<{TYPE}>instance({supplier})
    public static <T, K extends T> InstanceConfiguration<T> instance(Class<T>clazz, Supplier<K> instanceSupplier) {
        return new InstanceConfiguration<>(instanceSupplier);
    }

    /**
     * Can be an alternative to type Initialize.<{TYPE}>instance({supplier}), but namely this allows to pick up
     * generic types declared in sample instance and use them in auto-generation based on class type. Probably only
     * useful on root instance.
     *
     * If you use other creations and have instance say:
     *
     * <pre>
     *     class A<T> {
     *         T t;
     *     }
     * </pre>
     *
     * and instantiated it like: {@code A<List<List<String>> a = new A<>()} the type of {@code} would be impossible
     * to determine, which will happen if you use method say {@link #instance(Class)}. However if you use this method
     * and pass specially created sample instance, it will be possible to say, that {@code t} is {@code List<List<String>>}.
     * You need to call it like this: {@code instance(new A<List<List<String>(){}, A::new);} Note: yes, the anonymous
     * subclass is the needed trick.
     */
    public static <T, K extends T> InstanceConfiguration<T> instance(T sampleInstance, Supplier<K> instanceSupplier) {
        return new InstanceConfiguration<>(instanceSupplier);
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
        return new CollectionConfiguration<>(listInstanceCreationFunction, itemGenerator);
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
        return new CollectionConfiguration<>(listInstanceCreationFunction, itemGenerator);
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
        return new CollectionConfiguration<>(listInstanceCreationFunction, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, ITEM_TYPE[]> array(Class<ITEM_TYPE> arrayType, Generator<ITEM_TYPE> itemGenerator) {
        return array(items -> ReflectUtil.createArray(arrayType, items), itemGenerator);
    }

    private static <ITEM_TYPE> CollectionConfiguration<ITEM_TYPE, ITEM_TYPE[]> array(Function<Collection<? extends ITEM_TYPE>, ITEM_TYPE[]> arrayInstanceCreationFunction, Generator<ITEM_TYPE> itemGenerator) {
        return new CollectionConfiguration<>(arrayInstanceCreationFunction, itemGenerator);
    }
}
