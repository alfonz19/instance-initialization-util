package com.gmail.alfonz19.util.initialize.util;

import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public enum IntrospectorCache {
    INSTANCE;
    private final Map<Class<?>, BeanInfo> beanInfoCache = new HashMap<>();
    private final Map<Class<?>, Map<String, PropertyDescriptor>> propertyDescriptorByMethodNameMap = new HashMap<>();

    public PropertyDescriptor getPropertyDescriptor(Class<?> classType, String methodName) {
        Map<String, PropertyDescriptor> propertyDescriptorMap = getMethodNameToPropertyDescriptorMap(classType);
        return propertyDescriptorMap == null ? null : propertyDescriptorMap.get(methodName);
    }

    public Map<String, PropertyDescriptor> getMethodNameToPropertyDescriptorMap(Class<?> classType) {
        if (!beanInfoCache.containsKey(classType)) {
            init(classType);
        }
        return this.propertyDescriptorByMethodNameMap.get(classType);
    }

    private BeanInfo getBeanInfo(Class<?> classType) {
        BeanInfo beanInfo = this.beanInfoCache.get(classType);
        if (beanInfo == null && this.beanInfoCache.containsKey(classType)) {
            //we're caching `null` value.
            return beanInfo;
        }

        return init(classType);
    }

    private BeanInfo init(Class<?> classType) {
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(classType, Object.class);
        } catch (IntrospectionException e) {
            log.error("Unable to get BeanInfo of {}", classType);
            beanInfo = null;
        }

        this.beanInfoCache.put(classType, beanInfo);
        initPropertyDescriptorByMethodNameMap(classType, beanInfo);
        return beanInfo;
    }

    private void initPropertyDescriptorByMethodNameMap(Class<?> classType, BeanInfo beanInfo) {
        Map<String, PropertyDescriptor> propertyDescriptorsByMethodName = beanInfo == null
                ? null
                : Arrays.stream(beanInfo.getPropertyDescriptors())
                        .filter(e -> e.getReadMethod() != null && e.getWriteMethod() != null)
                        .collect(Collectors.toMap(ThrowingFunction.wrapInException(
                                (PropertyDescriptor propertyDescriptor) -> propertyDescriptor.getReadMethod().getName(),
                                InitializeException::new),
                                Function.identity()));
        this.propertyDescriptorByMethodNameMap.put(classType, propertyDescriptorsByMethodName);
    }

    public Collection<PropertyDescriptor> getPropertyDescriptorsComplyingToType(Class<?> classType,
                                                                                Class<?> propertyDescriptorType) {
        Collection<PropertyDescriptor> allPropertyDescriptors =
                getMethodNameToPropertyDescriptorMap(classType).values();
        return allPropertyDescriptors.stream()
                .filter(e -> e.getPropertyType().isAssignableFrom(propertyDescriptorType))
                .collect(Collectors.toList());
    }
}
