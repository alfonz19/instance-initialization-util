package com.gmail.alfonz19.util.initialize.exception;

public class SpecificTypePropertySelectorDoesNotDenoteProperty extends InitializeException {
    public <T> SpecificTypePropertySelectorDoesNotDenoteProperty(Class<? extends T> classType, String methodName) {
        super(String.format("SpecificTypePropertySelector in class '%s' does not select property, method '%s' s probably just a getter.", classType.getName(), methodName));
    }
}
