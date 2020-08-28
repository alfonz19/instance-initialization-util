package com.gmail.alfonz19.util.initialize.builder;

import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.Generators;

import java.util.function.Consumer;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class PropertyConfiguration<PROPERTY_TYPE, PARENT_BUILDER>
        extends BuilderWithParentBuilderReference<PARENT_BUILDER> {

    private final Consumer<Generator<PROPERTY_TYPE>> generatorSetCallback;

    public PropertyConfiguration(PARENT_BUILDER parentBuilder, Consumer<Generator<PROPERTY_TYPE>> generatorSetCallback) {
        super(parentBuilder);
        this.generatorSetCallback = generatorSetCallback;
    }

    public PARENT_BUILDER toValue(PROPERTY_TYPE value) {
        return to(Generators.constantGenerator(value));
    }

    public PARENT_BUILDER toNull() {
        return to(Generators.nullGenerator());
    }

    public PARENT_BUILDER to(Generator<PROPERTY_TYPE> valueGenerator) {
        generatorSetCallback.accept(valueGenerator);
        return getParentBuilder();
    }
}
