package com.gmail.alfonz19.util.initialize.builder;

import com.gmail.alfonz19.util.initialize.context.PathComponent;
import com.gmail.alfonz19.util.initialize.context.PathContext;
import com.gmail.alfonz19.util.initialize.generator.AbstractGenerator;
import com.gmail.alfonz19.util.initialize.generator.Initialize;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import com.gmail.alfonz19.util.initialize.generator.SizeSpecification;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public GENERATES create(PathContext pathContext) {
        int numberOfItemsToBeGenerated = sizeSpecification.getRandomSizeAccordingToSpecification();

        if (!shuffled) {
            List<? extends ITEM_TYPE> items = IntStream.range(0, numberOfItemsToBeGenerated).boxed()
                    //create array-like subPaths using index.
                    .map(pathContext::createSubPathTraversingArray)
                    .map((Function<PathContext, ITEM_TYPE>) pt -> Initialize.initialize(itemGenerator, pt))
                    .collect(Collectors.toList());
            return listInstanceSupplier.apply(items);
        }

        //shuffling is implemented as a trick. We cannot influence what generator will generate. Generator can generate
        //values in some orderly manner like 0,1,2, etc, which user might want to randomize. We cannot modify generator
        //context afterwards. We want to have items in generated list have adequate paths, ie. first item in list
        //will have context [0] and so on. So how to shuffle?
        //
        //We can generate context-related indices for all items, 0,1,2,... shuffle these, use them to create context paths
        //use generator to create value for each of them, potentially in orderly manner, but assigning it to randomized
        //context path, then ordering them back according to context-path. And we have ordered items according to
        //context path, but generator invocations isn't in sync with that.

        List<Integer> indices = IntStream.range(0, numberOfItemsToBeGenerated).boxed().collect(Collectors.toList());
        Collections.shuffle(indices);

        Map<PathContext, ? extends ITEM_TYPE> instancePathToGeneratedValue = indices.stream()
                .map(pathContext::createSubPathTraversingArray)
                .collect(Collectors.toMap(Function.identity(),
                        (Function<PathContext, ITEM_TYPE>) pt -> Initialize.initialize(itemGenerator, pt)));

        List<? extends ITEM_TYPE> shuffledItems = instancePathToGeneratedValue.keySet().stream()
                .sorted((o1, o2) -> {
                    PathComponent a = o1.getPath().getPathComponents().getLastPathComponent();
                    PathComponent b = o2.getPath().getPathComponents().getLastPathComponent();
                    if (a.isArray() && b.isArray()) {
                        return a.compareTo(b);
                    } else {
                        //no sorting, but this should not be reachable.
                        throw new InitializeException("Trying to shuffle non index-based path");
                    }
                })
                .map(instancePathToGeneratedValue::get)
                .collect(Collectors.toList());

        return listInstanceSupplier.apply(shuffledItems);
    }
}
