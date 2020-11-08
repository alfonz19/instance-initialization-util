package com.gmail.alfonz19.util.initialize.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gmail.alfonz19.util.initialize.builder.BuilderWithParentBuilderReference;
import com.gmail.alfonz19.util.initialize.builder.EnumConfiguration;
import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.InitializationConfiguration;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import com.gmail.alfonz19.util.initialize.rules.FindFirstApplicableRule;
import com.gmail.alfonz19.util.initialize.selector.SpecificTypePropertySelector;
import com.gmail.alfonz19.util.initialize.util.IntrospectorCache;
import com.gmail.alfonz19.util.initialize.util.InvocationSensor;
import com.gmail.alfonz19.util.initialize.util.ReflectUtil;
import com.gmail.alfonz19.util.initialize.util.TypeVariableAssignments;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@SuppressWarnings({"squid:S119"})//type variables
public class InstanceGenerator<SOURCE_INSTANCE> extends AbstractGenerator<SOURCE_INSTANCE> {

    private final Supplier<? extends SOURCE_INSTANCE> instanceSupplier;
    private InvocationSensor<SOURCE_INSTANCE> invocationSensor;
//    private final List<PropertyDescriptorInitialization> propertyDescriptorsInitializations = new LinkedList<>();
    private final Map<PropertyDescriptor, PropertyDescriptorInitialization> propertyDescriptorsInitializations = new LinkedHashMap<>();
    private final Class<SOURCE_INSTANCE> invocationSensorInstanceClass;

    public InstanceGenerator(Class<SOURCE_INSTANCE> sourceInstanceClass,
                             Supplier<? extends SOURCE_INSTANCE> instanceSupplier,
                             TypeReference<SOURCE_INSTANCE> typeReference) {
        this(sourceInstanceClass,
                typeReference.getType(),
                instanceSupplier, ReflectUtil.typeVariableAssignment(sourceInstanceClass, typeReference)
        );
    }

    public InstanceGenerator(Class<SOURCE_INSTANCE> sourceInstanceClass,
                             Supplier<? extends SOURCE_INSTANCE> instanceSupplier) {

        //noinspection unchecked
        Class<SOURCE_INSTANCE> instanceClassToUse = (sourceInstanceClass == null || sourceInstanceClass.isInterface())
                ? (Class<SOURCE_INSTANCE>) instanceSupplier.get().getClass()
                : sourceInstanceClass;

        this.invocationSensorInstanceClass = instanceClassToUse;
        this.instanceSupplier = instanceSupplier;
        setCalculatedNodeData(true, new CalculatedNodeData(instanceClassToUse,
                instanceClassToUse,
                ReflectUtil.getTypeVariableAssignment(instanceClassToUse)));

    }

    public InstanceGenerator(Class<SOURCE_INSTANCE> sourceInstanceClass,
                              Type genericClassType,
                              Supplier<? extends SOURCE_INSTANCE> instanceSupplier,
                              TypeVariableAssignments typeVariableAssignment) {
        this.invocationSensorInstanceClass = sourceInstanceClass;
        this.instanceSupplier = instanceSupplier;
        setCalculatedNodeData(true,
                new CalculatedNodeData(sourceInstanceClass, genericClassType, typeVariableAssignment));
    }

//    public InstanceGenerator(Supplier<? extends SOURCE_INSTANCE> instanceSupplier, CalculatedNodeData calculatedNodeData) {
//        this.invocationSensor = null;
//        this.instanceSupplier = instanceSupplier;
//        setCalculatedNodeData(true, calculatedNodeData);
//    }

    //TODO MMUCHA: reimplement
//    public final <K> InstanceGenerator<SOURCE_INSTANCE> referringToFieldUpContextPath(SpecificTypePropertySelector<SOURCE_INSTANCE, K> stringFieldSelector, int levelsUp) {
//        return this;
//    }
//
    //TODO MMUCHA: reimplement
//    public final <K, L> InstanceGenerator<SOURCE_INSTANCE> referringToFieldUpContextPath(SpecificTypePropertySelector<SOURCE_INSTANCE, K> stringFieldSelector, int levelsUp, Class<L> expectedTypeOfUpNode, SpecificTypePropertySelector<L, K> selector) {
//        return this;
//    }

    //TODO MMUCHA: reimplement
//    public final InstanceGenerator<SOURCE_INSTANCE> withPathContext(BiConsumer<InstanceGenerator<SOURCE_INSTANCE>, PathContext> withPathContext) {
//        withPathContext.accept(this, new PathContext());
//        return this;
//    }

    @Override
    public SOURCE_INSTANCE create(InitializationConfiguration initializationConfiguration, PathNode pathNode) {
        //path node might be different between invocation. Example 1 generator to generate N items in collection.
        //each item will have different pathNode.
        //calculatedNodeData will be always the same, but they need to be set at least one, and it cannot be done
        //sooner than here. So there is some inefficiency setting this variable multiple times.
        pathNode.setCalculatedNodeData(getCalculatedNodeData());


        SOURCE_INSTANCE instance = this.instanceSupplier.get();

        createPropertyInitializationBasedOnRulesAvailableForThisPath(instance, initializationConfiguration, pathNode);

        applyAllInitializations(instance, initializationConfiguration, pathNode);
        return instance;
    }

    private void applyAllInitializations(SOURCE_INSTANCE instance,
                                         InitializationConfiguration initializationConfiguration,
                                         PathNode pathNode) {
        this.propertyDescriptorsInitializations.values()
                .forEach(initializations -> initializations.init(instance, initializationConfiguration, pathNode));
    }

    private void createPropertyInitializationBasedOnRulesAvailableForThisPath(SOURCE_INSTANCE instance,
                                                                              InitializationConfiguration initializationConfiguration,
                                                                              PathNode pathNode) {
        List<PropertyDescriptor> unsetProperties = findUninitializedProperties();
        unsetProperties.forEach(propertyDescriptor -> {
            PathNode subPathNode = new PathNode.PropertyDescriptorBasedPathNode(pathNode, propertyDescriptor);

            FindFirstApplicableRule.getGeneratorFromFirstApplicableRule(instance, initializationConfiguration, subPathNode)
                    .ifPresent(generator -> addPropertyDescriptorInitialization(propertyDescriptor, generator));
        });
    }

    @SafeVarargs
    public final InstanceGenerator<SOURCE_INSTANCE> skipProperty(SpecificTypePropertySelector<SOURCE_INSTANCE, ?> ... propertySelectors){
        return skipProperties(Arrays.asList(propertySelectors));
    }

    public final InstanceGenerator<SOURCE_INSTANCE> skipProperties(List<SpecificTypePropertySelector<SOURCE_INSTANCE, ?>> propertySelectors){
        getInvocationSensor().getTouchedPropertyDescriptors(propertySelectors)
                .forEach(this::addSkippingPropertyDescriptorInitialization);
        return this;
    }

    @SafeVarargs
    public final InstanceGenerator<SOURCE_INSTANCE> nullifyProperty(SpecificTypePropertySelector<SOURCE_INSTANCE, ?> ... propertySelectors){
        return nullifyProperties(Arrays.asList(propertySelectors));
    }

    public InstanceGenerator<SOURCE_INSTANCE> nullifyProperties(List<SpecificTypePropertySelector<SOURCE_INSTANCE, ?>> propertySelectors) {
        Collection<PropertyDescriptor> propertyDescriptors = getInvocationSensor().getTouchedPropertyDescriptors(propertySelectors);
        propertyDescriptors.forEach(e -> addPropertyDescriptorInitialization(e, Generators.nullGenerator()));
        return this;
    }

    public final InstanceGenerator<SOURCE_INSTANCE> skipAllProperties(){
        findUninitializedProperties().forEach(this::addSkippingPropertyDescriptorInitialization);
        return this;
    }

    public final InstanceGenerator<SOURCE_INSTANCE> nullifyAllProperties(){
        List<PropertyDescriptor> missingPropertyDescriptors = findUninitializedProperties();
        DefaultValueGenerator<?> valueGenerator = Generators.defaultValue();
        missingPropertyDescriptors.forEach(pd -> addPropertyDescriptorInitialization(pd, valueGenerator));
        return this;
    }

    public <PROPERTY_TYPE extends Enum<?>> EnumConfiguration<PROPERTY_TYPE, InstanceGenerator<SOURCE_INSTANCE>>
    setEnumProperty(SpecificTypePropertySelector<SOURCE_INSTANCE, PROPERTY_TYPE> propertySelector) {
        PropertyDescriptor propertyDescriptor = getInvocationSensor().getTouchedPropertyDescriptor(propertySelector);

        //noinspection unchecked   //should be fine.
        Class<PROPERTY_TYPE> propertyType = (Class<PROPERTY_TYPE>) propertyDescriptor.getPropertyType();
        return new EnumConfiguration<>(propertyType, this,
                valueGenerator -> addPropertyDescriptorInitialization(propertyDescriptor, valueGenerator));
    }

    public <PROPERTY_TYPE> PropertyConfiguration<PROPERTY_TYPE, InstanceGenerator<SOURCE_INSTANCE>>
    setProperty(SpecificTypePropertySelector<SOURCE_INSTANCE, PROPERTY_TYPE> propertySelector){
        PropertyDescriptor propertyDescriptor = getInvocationSensor().getTouchedPropertyDescriptor(propertySelector);

        return new PropertyConfiguration<>(this,
                valueGenerator -> {
                    log.debug("setting generator "+valueGenerator+" to property "+propertyDescriptor.getName());
                    addPropertyDescriptorInitialization(propertyDescriptor, valueGenerator);
                });
    }

    public <PROPERTY_TYPE> InstanceGenerator<SOURCE_INSTANCE> setPropertyTo(SpecificTypePropertySelector<SOURCE_INSTANCE, PROPERTY_TYPE> propertySelector, Generator<PROPERTY_TYPE> valueGenerator){
        PropertyDescriptor propertyDescriptor = getInvocationSensor().getTouchedPropertyDescriptor(propertySelector);
        addPropertyDescriptorInitialization(propertyDescriptor, valueGenerator);
        return this;
    }

    public <PROPERTY_TYPE> InstanceGenerator<SOURCE_INSTANCE> setPropertyToValue(SpecificTypePropertySelector<SOURCE_INSTANCE, PROPERTY_TYPE> propertySelector, PROPERTY_TYPE value){
        return setPropertyTo(propertySelector, Generators.constantGenerator(value));
    }

    public <PROPERTY_TYPE> InstanceGenerator<SOURCE_INSTANCE> setPropertyToNull(SpecificTypePropertySelector<SOURCE_INSTANCE, PROPERTY_TYPE> propertySelector){
        return setPropertyTo(propertySelector, Generators.nullGenerator());
    }

    public <K> PropertyConfiguration<K, InstanceGenerator<SOURCE_INSTANCE>> setAllPropertiesHavingType(Class<K> classType) {
        Collection<PropertyDescriptor> propertyDescriptorsHavingType =
                IntrospectorCache.INSTANCE.getPropertyDescriptorsComplyingToType(getSourceInstanceClass(), classType);

        return addGeneratorsToPropertyDescriptors(propertyDescriptorsHavingType);
    }

    public <K> PropertyConfiguration<K, InstanceGenerator<SOURCE_INSTANCE>> setUnsetPropertiesHavingType(Class<K> classType) {
        List<PropertyDescriptor> missingPropertyDescriptors = findUninitializedProperties(classType);

        return addGeneratorsToPropertyDescriptors(missingPropertyDescriptors);
    }

    public InstanceGenerator<SOURCE_INSTANCE> setUnsetPropertiesRandomly() {
        findUninitializedProperties()
                .forEach(pd->addPropertyDescriptorInitialization(pd, Generators.randomValueOrDefault()));

        return this;
    }

    private InvocationSensor<SOURCE_INSTANCE> getInvocationSensor() {
        if (invocationSensor == null) {
            this.invocationSensor = new InvocationSensor<>(invocationSensorInstanceClass);
        }
        return invocationSensor;
    }

    @AllArgsConstructor
    @Data
    public static class Pair<T,K> {
        private T first;
        private K second;
    }

    public List<PropertyDescriptor> findUninitializedProperties() {
        return findUninitializedProperties(IntrospectorCache.INSTANCE.getAllPropertyDescriptors(getSourceInstanceClass()));
    }

    private <K> List<PropertyDescriptor> findUninitializedProperties(Class<K> classType) {
        Collection<PropertyDescriptor> propertyDescriptorsHavingType =
                IntrospectorCache.INSTANCE.getPropertyDescriptorsComplyingToType(getSourceInstanceClass(), classType);

        return findUninitializedProperties(propertyDescriptorsHavingType);
    }

    private List<PropertyDescriptor> findUninitializedProperties(Collection<PropertyDescriptor> propertyDescriptorsHavingType) {
        Set<PropertyDescriptor> alreadySpecifiedPropertyDescriptors = findSpecifiedDescriptors();

        return propertyDescriptorsHavingType.stream()
                .filter(e -> !alreadySpecifiedPropertyDescriptors.contains(e))
                .collect(Collectors.toList());
    }

    private Set<PropertyDescriptor> findSpecifiedDescriptors() {
        return propertyDescriptorsInitializations.keySet();
    }


    private <K> PropertyConfiguration<K, InstanceGenerator<SOURCE_INSTANCE>> addGeneratorsToPropertyDescriptors(
            Collection<PropertyDescriptor> propertyDescriptors) {
        return new PropertyConfiguration<>(this, valueGenerator ->
                propertyDescriptors.forEach(pd -> addPropertyDescriptorInitialization(pd, valueGenerator)));
    }

    private Class<?> getSourceInstanceClass() {
        return getCalculatedNodeData().getClassType();
    }

    private void addPropertyDescriptorInitialization(PropertyDescriptor propertyDescriptor,
                                                     Generator<?> valueGenerator) {
        PropertyDescriptorInitialization pdi = (instance, initializationConfiguration, pathNode) -> {
            PathNode.PropertyDescriptorBasedPathNode subNode =
                    new PathNode.PropertyDescriptorBasedPathNode(pathNode, propertyDescriptor);
            subNode.setValue(instance, GeneratorAccessor.create(valueGenerator, initializationConfiguration, subNode));

        };

        addPropertyDescriptorInitialization(propertyDescriptor, pdi);
    }

    private void addSkippingPropertyDescriptorInitialization(PropertyDescriptor propertyDescriptor) {
        PropertyDescriptorInitialization pdi = (instance, initializationConfiguration, pathNode) -> {
            //NO-OP
        };
        addPropertyDescriptorInitialization(propertyDescriptor, pdi);
    }

    private void addPropertyDescriptorInitialization(PropertyDescriptor propertyDescriptor,
                                                     PropertyDescriptorInitialization pdi) {
        testIfPropertyDescriptorIsNotUsed(propertyDescriptor);
        this.propertyDescriptorsInitializations.put(propertyDescriptor, pdi);
    }

    private void testIfPropertyDescriptorIsNotUsed(PropertyDescriptor propertyDescriptor) {
        if (this.propertyDescriptorsInitializations.containsKey(propertyDescriptor)) {
            throw new InitializeException(String.format("Property %s is already configured.", propertyDescriptor));
        }
    }

    @FunctionalInterface
    private interface PropertyDescriptorInitialization {
        void init(Object instance,
                  InitializationConfiguration initializationConfiguration,
                  PathNode pathNode);
    }

    public static class PropertyConfiguration<PROPERTY_TYPE, PARENT_BUILDER>
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
}
