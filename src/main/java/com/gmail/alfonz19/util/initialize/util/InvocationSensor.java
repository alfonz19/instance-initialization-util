package com.gmail.alfonz19.util.initialize.util;

import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import com.gmail.alfonz19.util.initialize.exception.SpecificTypePropertySelectorDoesNotDenoteProperty;
import com.gmail.alfonz19.util.initialize.selector.SpecificTypePropertySelector;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

@Slf4j
public class InvocationSensor<T> {

    private final T proxy;
    private final Map<String, PropertyDescriptor> touchedProperties = new HashMap<>();

    public InvocationSensor(T instance) {
        //noinspection unchecked
        this((Class<T>) instance.getClass());
    }

    public InvocationSensor(Class<T> classType) {
        Map<String, PropertyDescriptor> methodNameToPropertyDescriptorMap =
                IntrospectorCache.INSTANCE.getMethodNameToPropertyDescriptorMap(classType);

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(classType);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, methodProxy) -> {

            String methodName = method.getName();

            boolean propertyDescriptorExist =
                    IntrospectorCache.INSTANCE.getPropertyDescriptor(obj.getClass(), methodName) != null;

            if (propertyDescriptorExist) {
                touchedProperties.put(methodName, methodNameToPropertyDescriptorMap.get(methodName));
            } else {
                throw new SpecificTypePropertySelectorDoesNotDenoteProperty(classType, methodName);
            }

            return nullOrDefaultValue(method.getReturnType());
        });

        proxy = classType.cast(enhancer.create());
    }

    private void reset() {
        this.touchedProperties.clear();
    }

    private static Object nullOrDefaultValue(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            return DefaultPrimitiveValue.INSTANCE.get(returnType);
        } else {
            return null;
        }
    }

    public Collection<PropertyDescriptor> getTouchedPropertyDescriptors(List<SpecificTypePropertySelector<T, ?>> propertySelectors) {
        return propertySelectors.stream().map(this::getTouchedPropertyDescriptor).collect(Collectors.toList());
    }

    public PropertyDescriptor getTouchedPropertyDescriptor(SpecificTypePropertySelector<T, ?> propertySelector) {
        reset();
        applySelector(propertySelector);
        return getSoleTouchedPropertyDescriptor();
    }

    private void applySelector(SpecificTypePropertySelector<T, ?> propertySelector) {
        propertySelector.select(proxy);
    }

    private PropertyDescriptor getSoleTouchedPropertyDescriptor() {
        Collection<PropertyDescriptor> values = touchedProperties.values();
        int size = values.size();
        if (size != 1) {
            throw new InitializeException(String.format("Expected sole touched property, but %d properties were touched", size));
        }
        return values.iterator().next();
    }
}
