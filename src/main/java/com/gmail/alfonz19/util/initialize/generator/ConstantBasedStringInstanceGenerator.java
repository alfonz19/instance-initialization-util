package com.gmail.alfonz19.util.initialize.generator;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class ConstantBasedStringInstanceGenerator extends AbstractStringInstanceGenerator {
    public ConstantBasedStringInstanceGenerator(String initialValue) {
        //base transformation, turning whatever (null) to initial value.
        transformations.add((noValue, path) -> initialValue);
    }
}
