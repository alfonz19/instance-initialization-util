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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
//    private final List<PropertyDescriptorInitialization> propertyDescriptorsInitializations = new LinkedList<>();
    private final Map<PropertyDescriptor, PropertyDescriptorInitialization> propertyDescriptorsInitializations = new LinkedHashMap<>();
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
        //path node might be different between invocation. Example 1 generator to generate N items in collection.
        //each item will have different pathNode.
        //calculatedNodeData will be always the same, but they need to be set at least one, and it cannot be done
        //sooner than here. So there is some inefficiency setting this variable multiple times.
        pathNode.setCalculatedNodeData(calculatedNodeData);


        SOURCE_INSTANCE instance = this.instanceSupplier.get();

        applyRulesFromPathContext(pathNode);

        applyAllInitializations(instance, pathNode);
        return instance;
    }

    private void applyAllInitializations(SOURCE_INSTANCE instance, PathNode pathNode) {
        this.propertyDescriptorsInitializations.values()
                .forEach(initializations -> initializations.init(instance, pathNode));
    }

    private void applyRulesFromPathContext(PathNode pathNode) {
        List<PropertyDescriptor> unsetProperties = findUnsetProperties();
        unsetProperties.forEach(propertyDescriptor -> {
            PathNode subPathNode = new PathNode.PropertyDescriptorBasedPathNode(pathNode, propertyDescriptor);

            subPathNode.findFirstApplicableRule()
                    .ifPresent(rule -> addPropertyDescriptorInitialization(propertyDescriptor, rule.getGenerator()));
        });
    }

    private Stream<Rule> iteratorToStream(Iterator<Rule> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
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
        propertyDescriptors.forEach(e -> addPropertyDescriptorInitialization(e, Generators.nullGenerator()));
        return this;
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
                valueGenerator -> addPropertyDescriptorInitialization(propertyDescriptor, valueGenerator));
    }

    public <PROPERTY_TYPE> PropertyConfiguration<PROPERTY_TYPE, InstanceConfiguration<SOURCE_INSTANCE>>
    setProperty(SpecificTypePropertySelector<SOURCE_INSTANCE, PROPERTY_TYPE> propertySelector){
        PropertyDescriptor propertyDescriptor = invocationSensor.getTouchedPropertyDescriptor(propertySelector);

        return new PropertyConfiguration<>(this,
                valueGenerator -> {
                    log.debug("setting generator "+valueGenerator+" to property "+propertyDescriptor.getName());
                    addPropertyDescriptorInitialization(propertyDescriptor, valueGenerator);
                });
    }

    public <PROPERTY_TYPE> InstanceConfiguration<SOURCE_INSTANCE> setPropertyTo(SpecificTypePropertySelector<SOURCE_INSTANCE, PROPERTY_TYPE> propertySelector, AbstractGenerator<PROPERTY_TYPE> valueGenerator){
        PropertyDescriptor propertyDescriptor = invocationSensor.getTouchedPropertyDescriptor(propertySelector);
        addPropertyDescriptorInitialization(propertyDescriptor, valueGenerator);
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
        findUnsetProperties().stream()
                .filter(valueGenerator::canGenerateValueFor)
                .forEach(propertyDescriptor -> addPropertyDescriptorInitialization(propertyDescriptor, valueGenerator));

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
        Set<PropertyDescriptor> alreadySpecifiedPropertyDescriptors = findSpecifiedDescriptors();

        return propertyDescriptorsHavingType.stream()
                .filter(e -> !alreadySpecifiedPropertyDescriptors.contains(e))
                .collect(Collectors.toList());
    }

    private Set<PropertyDescriptor> findSpecifiedDescriptors() {
        return propertyDescriptorsInitializations.keySet();
    }


    private <K> PropertyConfiguration<K, InstanceConfiguration<SOURCE_INSTANCE>> addGeneratorsToPropertyDescriptors(
            Collection<PropertyDescriptor> propertyDescriptors) {
        return new PropertyConfiguration<>(this, valueGenerator ->
                propertyDescriptors.forEach(pd -> addPropertyDescriptorInitialization(pd, valueGenerator)));
    }

    ////TODO MMUCHA: fix, was probably inlined. revert if possible.
    private <K> Consumer<AbstractGenerator<K>> addPropertyDescriptorInitialization(PropertyDescriptor propertyDescriptor) {
        return valueGenerator -> addPropertyDescriptorInitialization(propertyDescriptor, valueGenerator);
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

    private void addPropertyDescriptorInitialization(PropertyDescriptor propertyDescriptor, AbstractGenerator<?> valueGenerator) {
        if (this.propertyDescriptorsInitializations.containsKey(propertyDescriptor)) {
            throw new InitializeException(String.format("Property %s is already configured.", propertyDescriptor));
        }

        PropertyDescriptorInitialization pdi = (instance, pathNode) -> {
            PathNode.PropertyDescriptorBasedPathNode subNode =
                    new PathNode.PropertyDescriptorBasedPathNode(pathNode, propertyDescriptor);
            subNode.setValue(instance, Initialize.initialize(valueGenerator, subNode));

        };
        this.propertyDescriptorsInitializations.put(propertyDescriptor, pdi);
    }

    @FunctionalInterface
    private interface PropertyDescriptorInitialization {
        void init(Object instance, PathNode pathNode);
    }
}
