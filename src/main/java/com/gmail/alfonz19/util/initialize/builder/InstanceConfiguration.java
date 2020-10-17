package com.gmail.alfonz19.util.initialize.builder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.PathNode;
import com.gmail.alfonz19.util.initialize.context.Rule;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import com.gmail.alfonz19.util.initialize.generator.AbstractGenerator;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import com.gmail.alfonz19.util.initialize.generator.Initialize;
import com.gmail.alfonz19.util.initialize.generator.RandomValueGenerator;
import com.gmail.alfonz19.util.initialize.selector.SpecificTypePropertySelector;
import com.gmail.alfonz19.util.initialize.util.IntrospectorCache;
import com.gmail.alfonz19.util.initialize.util.InvocationSensor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class InstanceConfiguration<SOURCE_INSTANCE> extends AbstractGenerator<SOURCE_INSTANCE> {

    private final Supplier<? extends SOURCE_INSTANCE> instanceSupplier;
    private final InvocationSensor<SOURCE_INSTANCE> invocationSensor;
    private final List<PropertyDescriptorInitialization> propertyDescriptorsInitializations = new LinkedList<>();
    private final CalculatedNodeData calculatedNodeData;

    public InstanceConfiguration(Class<SOURCE_INSTANCE> sourceInstanceClass,
                                 Supplier<? extends SOURCE_INSTANCE> instanceSupplier,
                                 TypeReference<SOURCE_INSTANCE> typeReference) {
        this(sourceInstanceClass, instanceSupplier, typeVariableAssignment(sourceInstanceClass, typeReference));
    }

    public InstanceConfiguration(Class<SOURCE_INSTANCE> sourceInstanceClass,
                                  Supplier<? extends SOURCE_INSTANCE> instanceSupplier) {
        this(sourceInstanceClass, instanceSupplier, Collections.emptyMap());
    }

    private InstanceConfiguration(Class<SOURCE_INSTANCE> sourceInstanceClass,
                                  Supplier<? extends SOURCE_INSTANCE> instanceSupplier,
                                  Map<TypeVariable<?>, Type> typeVariableAssignment) {
        this.invocationSensor = new InvocationSensor<>(sourceInstanceClass);
        this.instanceSupplier = instanceSupplier;
        calculatedNodeData = new CalculatedNodeData(sourceInstanceClass, typeVariableAssignment);
    }

    //TODO MMUCHA: reimplement
//    public final <K> InstanceConfiguration<SOURCE_INSTANCE> referringToFieldUpContextPath(SpecificTypePropertySelector<SOURCE_INSTANCE, K> stringFieldSelector, int levelsUp) {
//        return this;
//    }
//
    //TODO MMUCHA: reimplement
//    public final <K, L> InstanceConfiguration<SOURCE_INSTANCE> referringToFieldUpContextPath(SpecificTypePropertySelector<SOURCE_INSTANCE, K> stringFieldSelector, int levelsUp, Class<L> expectedTypeOfUpNode, SpecificTypePropertySelector<L, K> selector) {
//        return this;
//    }

    //TODO MMUCHA: reimplement
//    public final InstanceConfiguration<SOURCE_INSTANCE> withPathContext(BiConsumer<InstanceConfiguration<SOURCE_INSTANCE>, PathContext> withPathContext) {
//        withPathContext.accept(this, new PathContext());
//        return this;
//    }

    @Override
    public SOURCE_INSTANCE create(PathNode pathNode) {
        pathNode.setCalculatedNodeData(calculatedNodeData);


        SOURCE_INSTANCE instance = this.instanceSupplier.get();

        applyRulesFromPathContext(pathNode);

        applyAllInitializations(instance, pathNode);
        return instance;
    }

    private void applyAllInitializations(SOURCE_INSTANCE instance, PathNode pathNode) {
        this.propertyDescriptorsInitializations.forEach(initializations -> initializations.apply(instance, pathNode));
    }

    private void applyRulesFromPathContext(PathNode pathNode) {
        List<PropertyDescriptor> unsetProperties = findUnsetProperties();
        unsetProperties.forEach(propertyDescriptor -> {
            PathNode subPathNode = new PathNode.PropertyDescriptorBasedPathNode(pathNode, propertyDescriptor);

            subPathNode.findFirstApplicableRule()
                    .ifPresent(rule->addPropertyDescriptorsInitialization(propertyDescriptor, rule.getGenerator()));
        });
    }

    private Stream<Rule> iteratorToStream(Iterator<Rule> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

    private void addPropertyDescriptorsInitialization(PropertyDescriptor propertyDescriptor, AbstractGenerator<?> valueGenerator) {
        addPropertyDescriptorsInitializations(Collections.singletonList(
                new PropertyDescriptorInitialization(propertyDescriptor, valueGenerator)));
    }

    private void addPropertyDescriptorsInitializations(List<PropertyDescriptorInitialization> initializations) {
        verifyIfPropertyDescriptorIsAlreadyUsed(initializations);
        propertyDescriptorsInitializations.addAll(initializations);
    }

    private void verifyIfPropertyDescriptorIsAlreadyUsed(List<PropertyDescriptorInitialization> initializations) {
        List<PropertyDescriptor> alreadySpecifiedPropertyDescriptors = findSpecifiedDescriptors();

        String alreadyUsedProperties = initializations.stream()
                .filter(e -> alreadySpecifiedPropertyDescriptors.contains(e.propertyDescriptor))
                .map(e -> e.getPropertyDescriptor().getName())
                .collect(Collectors.joining(", "));

        if (!alreadyUsedProperties.isEmpty()) {
            throw new InitializeException(String.format("Properties %s are already configured for initialization",
                    alreadyUsedProperties));
        }
    }

    @SafeVarargs
    public final InstanceConfiguration<SOURCE_INSTANCE> skipProperty(SpecificTypePropertySelector<SOURCE_INSTANCE, ?> ... stringFieldSelector){
        return this;
    }

    @SafeVarargs
    public final InstanceConfiguration<SOURCE_INSTANCE> nullifyProperty(SpecificTypePropertySelector<SOURCE_INSTANCE, ?> ... stringFieldSelectors){
        return nullifyProperties(Arrays.asList(stringFieldSelectors));
    }

    public InstanceConfiguration<SOURCE_INSTANCE> nullifyProperties(List<SpecificTypePropertySelector<SOURCE_INSTANCE, ?>> propertySelectors) {
        Collection<PropertyDescriptor> propertyDescriptors = invocationSensor.getTouchedPropertyDescriptors(propertySelectors);
        addPropertyDescriptorsInitializations(createNullifyingTransformation(propertyDescriptors));
        return this;
    }

    private List<PropertyDescriptorInitialization> createNullifyingTransformation(Collection<PropertyDescriptor> propertyDescriptors) {
        return propertyDescriptors.stream()
                .map(e -> new PropertyDescriptorInitialization(e, Generators.nullGenerator()))
                .collect(Collectors.toList());
    }

    public final InstanceConfiguration<SOURCE_INSTANCE> skipAllProperties(){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public final InstanceConfiguration<SOURCE_INSTANCE> nullifyAllProperties(){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public <PROPERTY_TYPE extends Enum<?>> EnumConfiguration<PROPERTY_TYPE, InstanceConfiguration<SOURCE_INSTANCE>>
    setEnumProperty(SpecificTypePropertySelector<SOURCE_INSTANCE, PROPERTY_TYPE> propertySelector) {
        PropertyDescriptor propertyDescriptor = invocationSensor.getTouchedPropertyDescriptor(propertySelector);

        //noinspection unchecked   //should be fine.
        Class<PROPERTY_TYPE> propertyType = (Class<PROPERTY_TYPE>) propertyDescriptor.getPropertyType();
        return new EnumConfiguration<>(propertyType, this,
                valueGenerator -> addPropertyDescriptorsInitialization(propertyDescriptor, valueGenerator));
    }

    public <PROPERTY_TYPE> PropertyConfiguration<PROPERTY_TYPE, InstanceConfiguration<SOURCE_INSTANCE>>
    setProperty(SpecificTypePropertySelector<SOURCE_INSTANCE, PROPERTY_TYPE> propertySelector){
        PropertyDescriptor propertyDescriptor = invocationSensor.getTouchedPropertyDescriptor(propertySelector);

        return new PropertyConfiguration<>(this,
                valueGenerator -> {
                    log.debug("setting generator "+valueGenerator+" to property "+propertyDescriptor.getName());
                    addPropertyDescriptorsInitialization(propertyDescriptor, valueGenerator);
                });
    }

    public <PROPERTY_TYPE> InstanceConfiguration<SOURCE_INSTANCE> setPropertyTo(SpecificTypePropertySelector<SOURCE_INSTANCE, PROPERTY_TYPE> propertySelector, AbstractGenerator<PROPERTY_TYPE> valueGenerator){
        PropertyDescriptor propertyDescriptor = invocationSensor.getTouchedPropertyDescriptor(propertySelector);
        addPropertyDescriptorsInitialization(propertyDescriptor, valueGenerator);
        return this;
    }

    public <PROPERTY_TYPE> InstanceConfiguration<SOURCE_INSTANCE> setPropertyToValue(SpecificTypePropertySelector<SOURCE_INSTANCE, PROPERTY_TYPE> propertySelector, PROPERTY_TYPE value){
        return setPropertyTo(propertySelector, Generators.constantGenerator(value));
    }

    public <PROPERTY_TYPE> InstanceConfiguration<SOURCE_INSTANCE> setPropertyToNull(SpecificTypePropertySelector<SOURCE_INSTANCE, PROPERTY_TYPE> propertySelector){
        return setPropertyTo(propertySelector, Generators.nullGenerator());
    }

    public <K> PropertyConfiguration<K, InstanceConfiguration<SOURCE_INSTANCE>> setAllPropertiesHavingType(Class<K> classType) {
        Collection<PropertyDescriptor> propertyDescriptorsHavingType =
                IntrospectorCache.INSTANCE.getPropertyDescriptorsComplyingToType(getSourceInstanceClass(), classType);

        return addGeneratorsToPropertyDescriptors(propertyDescriptorsHavingType);
    }

    public <K> PropertyConfiguration<K, InstanceConfiguration<SOURCE_INSTANCE>> setUnsetPropertiesHavingType(Class<K> classType) {
        List<PropertyDescriptor> missingPropertyDescriptors = findUnsetProperties(classType);

        return addGeneratorsToPropertyDescriptors(missingPropertyDescriptors);
    }

    public InstanceConfiguration<SOURCE_INSTANCE> setUnsetPropertiesRandomlyUsingGuessedType() {
        RandomValueGenerator valueGenerator = Generators.randomForGuessedType();
        addPropertyDescriptorsInitializations(findUnsetProperties().stream()
                .filter(valueGenerator::canGenerateValueFor)
                .map(propertyDescriptor -> new PropertyDescriptorInitialization(propertyDescriptor, valueGenerator))
                .collect(Collectors.toList()));

        return this;
    }

    public List<PropertyDescriptor> findUnsetProperties() {
        return findUnsetProperties(IntrospectorCache.INSTANCE.getAllPropertyDescriptors(getSourceInstanceClass()));
    }

    private <K> List<PropertyDescriptor> findUnsetProperties(Class<K> classType) {
        Collection<PropertyDescriptor> propertyDescriptorsHavingType =
                IntrospectorCache.INSTANCE.getPropertyDescriptorsComplyingToType(getSourceInstanceClass(), classType);

        return findUnsetProperties(propertyDescriptorsHavingType);
    }

    private List<PropertyDescriptor> findUnsetProperties(Collection<PropertyDescriptor> propertyDescriptorsHavingType) {
        List<PropertyDescriptor> alreadySpecifiedPropertyDescriptors = findSpecifiedDescriptors();

        return propertyDescriptorsHavingType.stream()
                .filter(e -> !alreadySpecifiedPropertyDescriptors.contains(e))
                .collect(Collectors.toList());
    }

    private List<PropertyDescriptor> findSpecifiedDescriptors() {
        return propertyDescriptorsInitializations.stream()
                .map(PropertyDescriptorInitialization::getPropertyDescriptor)
                .collect(Collectors.toList());
    }


    private <K> PropertyConfiguration<K, InstanceConfiguration<SOURCE_INSTANCE>> addGeneratorsToPropertyDescriptors(
            Collection<PropertyDescriptor> propertyDescriptors) {
        return new PropertyConfiguration<>(this, valueGenerator -> addPropertyDescriptorsInitializations(
                propertyDescriptors.stream()
                        .map(pd -> new PropertyDescriptorInitialization(pd, valueGenerator))
                        .collect(Collectors.toList())));
    }


    ////TODO MMUCHA: fix, was probably inlined. revert if possible.
    private <K> Consumer<AbstractGenerator<K>> addPropertyDescriptorInitialization(PropertyDescriptor propertyDescriptor) {
        return valueGenerator -> addPropertyDescriptorsInitialization(propertyDescriptor, valueGenerator);
    }

    private static <SOURCE_INSTANCE> Map<TypeVariable<?>, Type> typeVariableAssignment(Class<SOURCE_INSTANCE> sourceInstanceClass,
                                                                                       TypeReference<SOURCE_INSTANCE> typeReference) {
        TypeVariable<Class<SOURCE_INSTANCE>>[] typeParameters = sourceInstanceClass.getTypeParameters();
        Type[] actualTypeArguments = ((ParameterizedType) typeReference.getType()).getActualTypeArguments();


        int typeParametersLength = typeParameters.length;
        int actualTypeArgumenstLength = actualTypeArguments.length;
        if (typeParametersLength != actualTypeArgumenstLength) {
            throw new IllegalArgumentException("Weird state, lenght of type parameters and it's actual values differs");
        }

        Map<TypeVariable<?>, Type> pairing = new HashMap<>(typeParametersLength);
        for(int i = 0; i < typeParametersLength; i++) {
            pairing.put(typeParameters[i], actualTypeArguments[i]);
        }

        return pairing;
    }

    private Class<?> getSourceInstanceClass() {
        return calculatedNodeData.getClassType();
    }

    @Getter
    private static class PropertyDescriptorInitialization {
        private final PropertyDescriptor propertyDescriptor;
        private final AbstractGenerator<?> valueGenerator;

        public PropertyDescriptorInitialization(PropertyDescriptor propertyDescriptor,
                                                AbstractGenerator<?> valueGenerator) {
            this.propertyDescriptor = propertyDescriptor;
            this.valueGenerator = valueGenerator;
        }

        public void apply(Object instance, PathNode pathNode) {
            PathNode subNode = new PathNode.PropertyDescriptorBasedPathNode(pathNode, propertyDescriptor);
            subNode.setValue(instance, Initialize.initialize(valueGenerator, subNode));
        }
    }
}
