package com.gmail.alfonz19.util.initialize.builder;

import com.gmail.alfonz19.util.initialize.generator.EnumInstanceGenerator;
import com.gmail.alfonz19.util.initialize.generator.Generator;

import java.util.function.Consumer;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class EnumConfiguration<T, PARENT_BUILDER> extends BuilderWithParentBuilderReference<PARENT_BUILDER> {

    private final EnumInstanceGenerator<T, T> enumInstanceGenerator;
    private final Consumer<Generator<T>> generatorSetCallback;

    public EnumConfiguration(Class<T> classType,
                             PARENT_BUILDER parentBuilder,
                             Consumer<Generator<T>> generatorSetCallback) {
        super(parentBuilder);
        enumInstanceGenerator = new EnumInstanceGenerator<>(classType, classType);
        this.generatorSetCallback = generatorSetCallback;
    }

    public PARENT_BUILDER random() {
        EnumInstanceGenerator<T, T> generator = enumInstanceGenerator.random();
        generatorSetCallback.accept(generator);
        return getParentBuilder();
    }

    @SafeVarargs
    public final PARENT_BUILDER randomFrom(T... values) {
        EnumInstanceGenerator<T, T> generator = enumInstanceGenerator.randomFrom(values);
        generatorSetCallback.accept(generator);

        return getParentBuilder();
    }
}
