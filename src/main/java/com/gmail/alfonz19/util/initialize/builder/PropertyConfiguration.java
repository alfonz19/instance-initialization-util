package com.gmail.alfonz19.util.initialize.builder;

import com.gmail.alfonz19.util.initialize.generator.AbstractGenerator;
import com.gmail.alfonz19.util.initialize.generator.Generators;

import java.util.function.Consumer;

//TODO MMUCHA: move to inner.
@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class PropertyConfiguration<PROPERTY_TYPE, PARENT_BUILDER>
        extends BuilderWithParentBuilderReference<PARENT_BUILDER> {

    private final Consumer<AbstractGenerator<PROPERTY_TYPE>> generatorSetCallback;

    public PropertyConfiguration(PARENT_BUILDER parentBuilder, Consumer<AbstractGenerator<PROPERTY_TYPE>> generatorSetCallback) {
        super(parentBuilder);
        this.generatorSetCallback = generatorSetCallback;
    }

    public PARENT_BUILDER toValue(PROPERTY_TYPE value) {
        return to(Generators.constantGenerator(value));
    }

    public PARENT_BUILDER toNull() {
        return to(Generators.nullGenerator());
    }

    public PARENT_BUILDER to(AbstractGenerator<PROPERTY_TYPE> valueGenerator) {
        generatorSetCallback.accept(valueGenerator);
        return getParentBuilder();
    }
}
