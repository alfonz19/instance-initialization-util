package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.Config;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class RandomStringInstanceGenerator extends  AbstractStringInstanceGenerator{
    private final MinMaxSpecification<Integer> sizeSpecification =
            MinMaxSpecification.intMinMaxSpecification(0, Config.MAX_STRING_LENGTH, Config.UNCONFIGURED_STRING_SIZE);

    public RandomStringInstanceGenerator() {
        //base transformation, turning whatever (null) to initial value.
        transformations.add((noValue, path) -> RandomUtil.INSTANCE.randomStringFromDecimalNumbers(
                sizeSpecification.getRandomValueAccordingToSpecification()));
    }

    public RandomStringInstanceGenerator withMinSize(int minLength) {
        sizeSpecification.setRequestedMin(minLength);
        return this;
    }

    public RandomStringInstanceGenerator withMaxSize(int maxLength) {
        sizeSpecification.setRequestedMax(maxLength);
        return this;
    }

    public RandomStringInstanceGenerator withSize(int length) {
        sizeSpecification.setRequestedValue(length);
        return this;
    }
}
