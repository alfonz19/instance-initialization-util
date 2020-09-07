package com.gmail.alfonz19.util.initialize.builder;

import com.gmail.alfonz19.util.initialize.generator.AbstractGenerator;
import com.gmail.alfonz19.util.initialize.generator.Initialize;
import com.gmail.alfonz19.util.initialize.generator.SizeSpecification;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings({"java:S119", "squid:S1172", "unused", "squid:S1068", "FieldCanBeLocal"})
//type variables, unused method parameters, unused constructs, field can be converted to local variable, same
public class CollectionConfiguration<ITEM_TYPE, GENERATES> extends AbstractGenerator<GENERATES> {
    //TODO MMUCHA: externalize.
    private static final int MAX_COLLECTION_LENGTH = 100;
    //TODO MMUCHA: externalize.
    public static final int UNCONFIGURED_COLLECTION_SIZE = 5;

    private final Function<Collection<? extends ITEM_TYPE>, GENERATES> listInstanceSupplier;
    private final AbstractGenerator<? extends ITEM_TYPE> itemGenerator;
    private final SizeSpecification sizeSpecification =
            new SizeSpecification(0, MAX_COLLECTION_LENGTH, UNCONFIGURED_COLLECTION_SIZE);
    private boolean shuffled;

    public CollectionConfiguration(Function<Collection<? extends ITEM_TYPE>, GENERATES> listInstanceSupplier, AbstractGenerator<? extends ITEM_TYPE> itemGenerator) {
        this.listInstanceSupplier = listInstanceSupplier;
        this.itemGenerator = itemGenerator;
    }

    public CollectionConfiguration<ITEM_TYPE, GENERATES> withMinSize(int minSize) {
        sizeSpecification.setRequestedMinSize(minSize);
        return this;
    }

    public CollectionConfiguration<ITEM_TYPE, GENERATES> withMaxSize(int maxSize) {
        sizeSpecification.setRequestedMaxSize(maxSize);
        return this;
    }

    public CollectionConfiguration<ITEM_TYPE, GENERATES> withSize(int size) {
        sizeSpecification.setRequestedSize(size);
        return this;
    }

    public CollectionConfiguration<ITEM_TYPE, GENERATES> shuffled() {
        return shuffled(true);
    }

    public CollectionConfiguration<ITEM_TYPE, GENERATES> shuffled(boolean shuffle) {
        this.shuffled = shuffle;
        return this;
    }

    @Override
    protected GENERATES create() {
        List<? extends ITEM_TYPE> items = Initialize.initializeList(itemGenerator, sizeSpecification.getRandomSizeAccordingToSpecification());
        if (shuffled) {
            Collections.shuffle(items);
        }
        return listInstanceSupplier.apply(items);
    }
}
