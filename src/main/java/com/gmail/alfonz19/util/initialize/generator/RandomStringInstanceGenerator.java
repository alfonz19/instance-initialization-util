package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.util.RandomUtil;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class RandomStringInstanceGenerator extends  AbstractStringInstanceGenerator{
    //TODO MMUCHA: externalize
    private static final int MAX_STRING_LENGTH = 100_000;
    //TODO MMUCHA: externalize
    public static final int UNCONFIGURED_STRING_SIZE = 20;
    private final SizeSpecification sizeSpecification =
            new SizeSpecification(0, MAX_STRING_LENGTH, UNCONFIGURED_STRING_SIZE);

    public RandomStringInstanceGenerator() {
        //base transformation, turning whatever (null) to initial value.
        transformations.add((noValue, path) -> RandomUtil.INSTANCE.randomStringFromDecimalNumbers(
                sizeSpecification.getRandomSizeAccordingToSpecification()));
    }

    public RandomStringInstanceGenerator withMinSize(int minLength) {
        sizeSpecification.setRequestedMinSize(minLength);
        return this;
    }

    public RandomStringInstanceGenerator withMaxSize(int maxLength) {
        sizeSpecification.setRequestedMaxSize(maxLength);
        return this;
    }

    public RandomStringInstanceGenerator withSize(int length) {
        sizeSpecification.setRequestedSize(length);
        return this;
    }
}
