package com.gmail.alfonz19.util.initialize.builder;

import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import com.gmail.alfonz19.util.initialize.generator.AbstractGenerator;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import com.gmail.alfonz19.util.initialize.generator.Initialize;
import com.gmail.alfonz19.util.initialize.selector.SpecificTypePropertySelector;
import com.gmail.alfonz19.util.initialize.util.IntrospectorCache;
import com.gmail.alfonz19.util.initialize.util.InvocationSensor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class InstanceConfiguration<SOURCE_INSTANCE> extends AbstractGenerator<SOURCE_INSTANCE> {

    private final Supplier<? extends SOURCE_INSTANCE> instanceSupplier;
    private final InvocationSensor<SOURCE_INSTANCE> invocationSensor;
    private final List<PropertyDescriptorInitialization> propertyDescriptorsInitializations = new LinkedList<>();
    private final Class<?> sourceInstanceClazz;

    public InstanceConfiguration(Supplier<? extends SOURCE_INSTANCE> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
        SOURCE_INSTANCE sampleInstance = instanceSupplier.get();
        sourceInstanceClazz = sampleInstance.getClass();
        invocationSensor = new InvocationSensor<>(sampleInstance);
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
    protected SOURCE_INSTANCE create() {
        SOURCE_INSTANCE instance = this.instanceSupplier.get();
        applyAllInitializations(instance);
        return instance;
    }

    private void applyAllInitializations(SOURCE_INSTANCE instance) {
        this.propertyDescriptorsInitializations.forEach(initializations -> initializations.apply(instance));
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
        List<PropertyDescriptor> alreadySpecifiedPropertyDescriptors = propertyDescriptorsInitializations.stream()
                .map(PropertyDescriptorInitialization::getPropertyDescriptor)
                .collect(Collectors.toList());

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
        return new EnumConfiguration<>(propertyType, this, addPropertyDescriptorInitialization(propertyDescriptor));
    }

    public <PROPERTY_TYPE> PropertyConfiguration<PROPERTY_TYPE, InstanceConfiguration<SOURCE_INSTANCE>>
    setProperty(SpecificTypePropertySelector<SOURCE_INSTANCE, PROPERTY_TYPE> propertySelector){
        PropertyDescriptor propertyDescriptor = invocationSensor.getTouchedPropertyDescriptor(propertySelector);

        return new PropertyConfiguration<>(this, addPropertyDescriptorInitialization(propertyDescriptor));
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
                IntrospectorCache.INSTANCE.getPropertyDescriptorsComplyingToType(sourceInstanceClazz, classType);

        return addGeneratorsToPropertyDescriptors(propertyDescriptorsHavingType);
    }

    public <K> PropertyConfiguration<K, InstanceConfiguration<SOURCE_INSTANCE>> setUnsetPropertiesHavingType(Class<K> classType) {
        Collection<PropertyDescriptor> propertyDescriptorsHavingType =
                IntrospectorCache.INSTANCE.getPropertyDescriptorsComplyingToType(sourceInstanceClazz, classType);

        List<PropertyDescriptor> alreadySpecifiedPropertyDescriptors = propertyDescriptorsInitializations.stream()
                .map(PropertyDescriptorInitialization::getPropertyDescriptor)
                .collect(Collectors.toList());

        List<PropertyDescriptor> missingPropertyDescriptors = propertyDescriptorsHavingType.stream()
                .filter(e -> !alreadySpecifiedPropertyDescriptors.contains(e))
                .collect(Collectors.toList());

        return addGeneratorsToPropertyDescriptors(missingPropertyDescriptors);
    }

    private <K> PropertyConfiguration<K, InstanceConfiguration<SOURCE_INSTANCE>> addGeneratorsToPropertyDescriptors(
            Collection<PropertyDescriptor> propertyDescriptors) {
        return new PropertyConfiguration<>(this, valueGenerator -> addPropertyDescriptorsInitializations(
                propertyDescriptors.stream()
                        .map(pd -> new PropertyDescriptorInitialization(pd, valueGenerator))
                        .collect(Collectors.toList())));
    }

    private <K> Consumer<AbstractGenerator<K>> addPropertyDescriptorInitialization(PropertyDescriptor propertyDescriptor) {
        return valueGenerator -> addPropertyDescriptorsInitialization(propertyDescriptor, valueGenerator);
    }

    @Getter
    @AllArgsConstructor
    private static class PropertyDescriptorInitialization {
        private final PropertyDescriptor propertyDescriptor;
        private final AbstractGenerator<?> valueGenerator;

        public void apply(Object instance) {
            try {
                Object value = Initialize.initialize(valueGenerator);
                propertyDescriptor.getWriteMethod().invoke(instance, value);
            } catch (IllegalAccessException| InvocationTargetException e) {
                throw new InitializeException("Unable to use PropertyDescriptor writer method", e);
            }
        }
    }
}
