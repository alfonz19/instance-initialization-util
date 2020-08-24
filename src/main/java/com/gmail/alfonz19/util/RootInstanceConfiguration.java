package com.gmail.alfonz19.util;

import java.util.function.Supplier;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class RootInstanceConfiguration<SOURCE_INSTANCE> extends
        InstanceConfiguration<SOURCE_INSTANCE, RootInstanceConfiguration<SOURCE_INSTANCE>>{

    public RootInstanceConfiguration(Supplier<SOURCE_INSTANCE> source_instanceSupplier) {
        super(source_instanceSupplier);
    }

    @Override
    protected RootInstanceConfiguration<SOURCE_INSTANCE> getSelf() {
        return this;
    }

    public SOURCE_INSTANCE create() {
        return instanceSupplier.get();
    }
}
