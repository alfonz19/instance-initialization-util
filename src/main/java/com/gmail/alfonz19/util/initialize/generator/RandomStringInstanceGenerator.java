package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.Config;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class RandomStringInstanceGenerator extends  AbstractStringInstanceGenerator{
    private final SizeSpecification sizeSpecification =
            new SizeSpecification(0, Config.MAX_STRING_LENGTH, Config.UNCONFIGURED_STRING_SIZE);

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
