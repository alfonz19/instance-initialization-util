package com.gmail.alfonz19.util.initialize.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gmail.alfonz19.util.initialize.Config;
import com.gmail.alfonz19.util.initialize.Initialize;
import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.InitializationContext;
import com.gmail.alfonz19.util.initialize.context.path.PathComponent;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import com.gmail.alfonz19.util.initialize.rules.FindFirstApplicableRule;
import com.gmail.alfonz19.util.initialize.util.ReflectUtil;
import com.gmail.alfonz19.util.initialize.util.TypeReferenceUtil;
import com.gmail.alfonz19.util.initialize.util.TypeVariableAssignments;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.gmail.alfonz19.util.initialize.util.TypeVariableAssignments.NO_TYPE_VARIABLE_ASSIGNMENTS;

@SuppressWarnings({"java:S119", "squid:S1172", "unused", "squid:S1068", "FieldCanBeLocal"})
//type variables, unused method parameters, unused constructs, field can be converted to local variable, same
public class CollectionGenerator<ITEM_TYPE, GENERATES> extends AbstractGenerator<GENERATES> {

    private final Function<Collection<? extends ITEM_TYPE>, GENERATES> listInstanceSupplier;
    private final Generator<? extends ITEM_TYPE> itemGenerator;
    private final MinMaxSpecification<Integer> sizeSpecification =
            MinMaxSpecification.intMinMaxSpecification(0, Config.MAX_COLLECTION_LENGTH, Config.UNCONFIGURED_COLLECTION_SIZE);
    private boolean shuffled;
    //this states, whether it's probable, that we have more specific information about type than one passed via PathNode.
    private final boolean overwriteCalculatedNodeData;

    public CollectionGenerator(Function<Collection<? extends ITEM_TYPE>, GENERATES> listInstanceSupplier, TypeReference<GENERATES> typeReference) {
        this.listInstanceSupplier = listInstanceSupplier;
        this.itemGenerator = null;

        Class<GENERATES> classType = TypeReferenceUtil.getRawTypeClassType(typeReference);
        TypeVariableAssignments typeVariableAssignments = ReflectUtil.typeVariableAssignment(classType, typeReference);
        setCalculatedNodeData(true,
                new CalculatedNodeData(classType, typeReference.getType(), typeVariableAssignments));
        overwriteCalculatedNodeData = true;
    }

    public CollectionGenerator(Class<?> classType,
                               Function<Collection<? extends ITEM_TYPE>, GENERATES> listInstanceSupplier,
                               Generator<? extends ITEM_TYPE> itemGenerator) {
        this.listInstanceSupplier = listInstanceSupplier;
        this.itemGenerator = itemGenerator;

        setCalculatedNodeData(true, new CalculatedNodeData(classType, classType, NO_TYPE_VARIABLE_ASSIGNMENTS));
        this.overwriteCalculatedNodeData = false;
    }

    public CollectionGenerator<ITEM_TYPE, GENERATES> withMinSize(int minSize) {
        sizeSpecification.setRequestedMin(minSize);
        return this;
    }

    public CollectionGenerator<ITEM_TYPE, GENERATES> withMaxSize(int maxSize) {
        sizeSpecification.setRequestedMax(maxSize);
        return this;
    }

    public CollectionGenerator<ITEM_TYPE, GENERATES> withSize(int size) {
        sizeSpecification.setRequestedValue(size);
        return this;
    }

    public CollectionGenerator<ITEM_TYPE, GENERATES> shuffled() {
        return shuffled(true);
    }

    public CollectionGenerator<ITEM_TYPE, GENERATES> shuffled(boolean shuffle) {
        this.shuffled = shuffle;
        return this;
    }

    @Override
    protected GENERATES create(InitializationContext initializationContext, PathNode pathNode) {
        if (overwriteCalculatedNodeData || pathNode.getCalculatedNodeData() == null) {
            pathNode.setCalculatedNodeData(getCalculatedNodeData());
        }

        int numberOfItemsToBeGenerated = sizeSpecification.getRandomValueAccordingToSpecification();

        Generator<? extends ITEM_TYPE> itemGeneratorToUse = getGeneratorToUse(initializationContext, pathNode);

        CalculatedNodeData itemsCalculatedNodeData = (pathNode.getCalculatedNodeData().representsParameterizedType())
                ? ReflectUtil.unwrapParameterizedType(pathNode.getCalculatedNodeData())
                : GeneratorAccessor.getCalculatedNodeData(itemGeneratorToUse) == null
                        ? new CalculatedNodeData(Object.class, Object.class, NO_TYPE_VARIABLE_ASSIGNMENTS)
                        : GeneratorAccessor.getCalculatedNodeData(itemGeneratorToUse);

        if (!shuffled) {
            List<? extends ITEM_TYPE> items = IntStream.range(0, numberOfItemsToBeGenerated).boxed()
                    //create array-like subPaths using index.
                    .map(index -> new PathNode.CollectionItemNode(pathNode, index, itemsCalculatedNodeData))
                    .map((Function<PathNode, ITEM_TYPE>) pt -> Initialize.initialize(itemGeneratorToUse,
                            initializationContext,
                            pt))
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

        Map<PathNode, ? extends ITEM_TYPE> instancePathToGeneratedValue = indices.stream()
                .map(index -> new PathNode.CollectionItemNode(pathNode, index, itemsCalculatedNodeData))
                .collect(Collectors.toMap(Function.identity(),
                        (Function<PathNode, ITEM_TYPE>) pt -> Initialize.initialize(itemGeneratorToUse,
                                initializationContext,
                                pt)));

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

    private Generator<? extends ITEM_TYPE> getGeneratorToUse(InitializationContext initializationContext,
                                                             PathNode pathNode) {
        if (this.itemGenerator != null) {
            return itemGenerator;
        }

        //noinspection unchecked
        Generator<? extends ITEM_TYPE> itemGeneratorToUse =
                (Generator<? extends ITEM_TYPE>) FindFirstApplicableRule.getGeneratorFromFirstApplicableRule(null,
                        initializationContext,
                        pathNode).orElse(null);

        if (itemGeneratorToUse == null) {
            itemGeneratorToUse = Generators.defaultValue();
        }
        return itemGeneratorToUse;
    }
}
