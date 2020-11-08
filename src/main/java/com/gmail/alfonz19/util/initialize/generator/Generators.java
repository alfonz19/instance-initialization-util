package com.gmail.alfonz19.util.initialize.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gmail.alfonz19.util.initialize.context.InitializationConfiguration;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import com.gmail.alfonz19.util.initialize.rules.FindFirstApplicableRule;
import com.gmail.alfonz19.util.initialize.util.ReflectUtil;
import com.gmail.alfonz19.util.initialize.util.TypeReferenceUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
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

    public static ByteInstanceGenerator randomByte() {
        return new ByteInstanceGenerator();
    }

    public static ByteInstanceGenerator randomByteFromRange(byte min, byte max) {
        return new ByteInstanceGenerator().biggerThan(min).smallerThan(max);
    }

    public static <T> EnumInstanceGenerator<T, T> enumeratedType(Class<T> classType) {
        return new EnumInstanceGenerator<>(classType, classType);
    }

    public static <T> EnumInstanceGenerator<T, T> enumeratedType() {
        return new EnumInstanceGenerator<>();
    }

    public static <T extends K, K> EnumInstanceGenerator<T, K> enumeratedType(Class<T> classType, Class<K> interfaceEnumImplements) {
        return new EnumInstanceGenerator<>(classType, interfaceEnumImplements);
    }

    public static <T extends K, K> EnumInstanceGenerator<T, K> enumeratedType(Class<T> classType, TypeReference<K> interfaceEnumImplements) {
        return new EnumInstanceGenerator<>(classType, interfaceEnumImplements);
    }

    public static Generator<Object> randomValueOrFail() {
        return RVG.orFail();
    }

    public static Generator<Object> randomValueOrDefault() {
        return RVG.orDefault();
    }

    public static class RVG extends AbstractGenerator<Object> {
        private final BiFunction<PathNode, Optional<Generator<?>>, Generator<?>> finisher;
        private Generator<?> generator = null;

        public RVG(BiFunction<PathNode, Optional<Generator<?>>, Generator<?>> finisher) {
            this.finisher = finisher;
        }

        public static Generator<Object> orFail() {
            return new RVG((pathNode, e) -> e.orElseThrow(
                    () -> new InitializeException(String.format("Unable to initialize %s", pathNode.getPath().getPathAsString()))));
        }

        public static Generator<Object> orDefault() {
            return new RVG((pathNode, e) -> e.orElseGet(Generators::defaultValue));
        }

        @Override
        protected Object create(InitializationConfiguration initializationConfiguration, PathNode pathNode) {
            if (generator == null) {
                Optional<Generator<?>> firstApplicableRule =
                        FindFirstApplicableRule.getGeneratorFromFirstApplicableRandomGeneratorRule(null,
                                initializationConfiguration,
                                pathNode);
                generator = finisher.apply(pathNode, firstApplicableRule);
            }

            return GeneratorAccessor.create(generator, initializationConfiguration, pathNode);
        }
    }

    public static <T> DefaultValueGenerator<T> defaultValue() {
        return new DefaultValueGenerator<>();
    }

    @SafeVarargs
    public static <T> Generator<T> roundRobinGenerator(Generator<? extends T> ... generators) {
        if (generators == null || generators.length == 0) {
            throw new IllegalArgumentException();
        }
        return new RoundRobinGenerator<>(Arrays.asList(generators));
    }

    @SafeVarargs
    public static <T> Generator<T> sequentialGenerator(Generator<? extends T> ... generators) {
        if (generators == null || generators.length == 0) {
            throw new IllegalArgumentException();
        }
        return new SequentialGenerator<>(Arrays.asList(generators));
    }

    @SafeVarargs
    public static <T> Generator<T> randomlySelectedGenerator(Generator<T> ... generators) {
        if (generators == null || generators.length == 0) {
            throw new IllegalArgumentException();
        }
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

    public static <T> InstanceGenerator<T> instance(Supplier<T> instanceSupplier, TypeReference<T> typeReference) {
        return new InstanceGenerator<>(TypeReferenceUtil.getRawTypeClassType(typeReference), instanceSupplier, typeReference);
    }

    public static <T> InstanceGenerator<T> instance(TypeReference<T> typeReference) {
        Class<T> rawTypeClassType = TypeReferenceUtil.getRawTypeClassType(typeReference);
        Supplier<T> supplier = ReflectUtil.supplierFromClass(rawTypeClassType);
        return instance(supplier, typeReference);
    }

    public static <T> InstanceGenerator<T> instance(Supplier<T> instanceSupplier) {
        return instance(null, instanceSupplier);
    }

    public static <T> InstanceGenerator<T> instance(Class<T> instanceClass) {
        if (instanceClass.isInterface()) {
            throw new IllegalArgumentException("Cannot create generator from interface");
        }
        return instance(instanceClass, ReflectUtil.supplierFromClass(instanceClass));
    }

    //an alternative to type Generators.<{TYPE}>instance({supplier})
    public static <T, K extends T> InstanceGenerator<T> instance(Class<T>classType, Supplier<K> instanceSupplier) {
        return new InstanceGenerator<>(classType, instanceSupplier);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, List<ITEM_TYPE>> list(Class<ITEM_TYPE>clazz, Generator<ITEM_TYPE> itemGenerator) {
        return list(ArrayList::new, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, List<ITEM_TYPE>> list(Generator<? extends ITEM_TYPE> itemGenerator) {
        return list(ArrayList::new, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, List<ITEM_TYPE>> listWithNullItems(Class<ITEM_TYPE> itemClassType) {
        return list(ArrayList::new, nullGenerator());
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, List<ITEM_TYPE>> list(Function<Collection<? extends ITEM_TYPE>, List<ITEM_TYPE>> listInstanceCreationFunction,
                                                                                   Generator<? extends ITEM_TYPE> itemGenerator) {
        return new CollectionGenerator<>(List.class, listInstanceCreationFunction, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, List<ITEM_TYPE>> list(TypeReference<List<ITEM_TYPE>> typeReference) {
        return list(ArrayList::new, typeReference);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, List<ITEM_TYPE>> list(
            Function<Collection<? extends ITEM_TYPE>, List<ITEM_TYPE>> listInstanceCreationFunction,
            TypeReference<List<ITEM_TYPE>> typeReference) {

        return new CollectionGenerator<>(listInstanceCreationFunction, typeReference);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, Set<ITEM_TYPE>> set(Class<ITEM_TYPE>clazz, Generator<ITEM_TYPE> itemGenerator) {
        return set(HashSet::new, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, Set<ITEM_TYPE>> set(Generator<ITEM_TYPE> itemGenerator) {
        return set(HashSet::new, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, Set<ITEM_TYPE>> setWithNullItems(Class<ITEM_TYPE> itemClassType) {
        return set(HashSet::new, nullGenerator());
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, Set<ITEM_TYPE>> set(Function<Collection<? extends ITEM_TYPE>, Set<ITEM_TYPE>> listInstanceCreationFunction,
                                                                                 Generator<ITEM_TYPE> itemGenerator) {
        return new CollectionGenerator<>(Set.class, listInstanceCreationFunction, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, Set<ITEM_TYPE>> set(TypeReference<Set<ITEM_TYPE>> typeReference) {
        return set(HashSet::new, typeReference);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, Set<ITEM_TYPE>> set(
            Function<Collection<? extends ITEM_TYPE>, Set<ITEM_TYPE>> setInstanceCreationFunction,
            TypeReference<Set<ITEM_TYPE>> typeReference) {

        return new CollectionGenerator<>(setInstanceCreationFunction, typeReference);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, Stream<ITEM_TYPE>> stream(Class<ITEM_TYPE>clazz, Generator<ITEM_TYPE> itemGenerator) {
        return stream(c -> new ArrayList<ITEM_TYPE>(c).stream(), itemGenerator);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, Stream<ITEM_TYPE>> stream(Generator<ITEM_TYPE> itemGenerator) {
        return stream(c -> new ArrayList<ITEM_TYPE>(c).stream(), itemGenerator);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, Stream<ITEM_TYPE>> streamWithNullItems(Class<ITEM_TYPE> itemClassType) {
        return stream(c -> new ArrayList<ITEM_TYPE>(c).stream(), nullGenerator());
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, Stream<ITEM_TYPE>> stream(Function<Collection<? extends ITEM_TYPE>, Stream<ITEM_TYPE>> listInstanceCreationFunction,
                                                                                       Generator<ITEM_TYPE> itemGenerator) {
        return new CollectionGenerator<>(Stream.class, listInstanceCreationFunction, itemGenerator);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, Stream<ITEM_TYPE>> stream(TypeReference<Stream<ITEM_TYPE>> typeReference) {
        return stream(c -> new ArrayList<ITEM_TYPE>(c).stream(), typeReference);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, Stream<ITEM_TYPE>> stream(
            Function<Collection<? extends ITEM_TYPE>, Stream<ITEM_TYPE>> streamInstanceCreationFunction,
            TypeReference<Stream<ITEM_TYPE>> typeReference) {

        return new CollectionGenerator<>(streamInstanceCreationFunction, typeReference);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, ITEM_TYPE[]> array(Class<ITEM_TYPE> arrayType, Generator<ITEM_TYPE> itemGenerator) {
        return new CollectionGenerator<>(arrayType, items -> ReflectUtil.createArray(arrayType, items), itemGenerator);
    }

    public static <ITEM_TYPE> CollectionGenerator<ITEM_TYPE, ITEM_TYPE[]> array(Generator<ITEM_TYPE> itemGenerator) {
        //noinspection unchecked
        Class<ITEM_TYPE> arrayType = (Class<ITEM_TYPE>) GeneratorAccessor.getCalculatedNodeData(itemGenerator).getClassType();
        return array(arrayType, itemGenerator);
    }

    public static <T> Generator<T> generatorFromSupplier(Supplier<T> supplier) {
        return new AbstractGenerator<T>() {
            @Override
            protected T create(InitializationConfiguration initializationConfiguration, PathNode pathNode) {
                return supplier.get();
            }
        };
    }

    public static Generator<?> newInstanceViaNoArgConstructor(Class<?> classType) {
        return new NewInstanceGenerator(classType);
    }

    public static Generator<?> newInstanceViaNoArgConstructor() {
        return new NewInstanceGenerator();  //TODO MMUCHA: why is this in full auto test called multiple times??
    }
}
