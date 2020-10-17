package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.PathNode;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class StringInstanceGenerator extends AbstractGenerator<String> {
    //TODO MMUCHA: externalize
    private static final int MAX_STRING_LENGTH = 100_000;
    //TODO MMUCHA: externalize
    public static final int UNCONFIGURED_STRING_SIZE = 20;
    private final SizeSpecification sizeSpecification =
            new SizeSpecification(0, MAX_STRING_LENGTH, UNCONFIGURED_STRING_SIZE);

    private final List<UnaryOperator<String>> transformations = new LinkedList<>();

    public StringInstanceGenerator withMinSize(int minLength) {
        sizeSpecification.setRequestedMinSize(minLength);
        return this;
    }

    public StringInstanceGenerator withMaxSize(int maxLength) {
        sizeSpecification.setRequestedMaxSize(maxLength);
        return this;
    }

    public StringInstanceGenerator withSize(int length) {
        sizeSpecification.setRequestedSize(length);
        return this;
    }

    public StringInstanceGenerator addPrefix(String prefix) {
        transformations.add(string -> prefix + string);
        return this;
    }

    public StringInstanceGenerator addSuffix(String suffix) {
        transformations.add(string -> string + suffix);
        return this;
    }

    public StringInstanceGenerator updatedWithContext(BiFunction<String, PathNode, String> updatingFunction) {
        //TODO MMUCHA: implement!
        return this;
    }

    @Override
    protected String create(PathNode pathNode) {
        String result = RandomUtil.INSTANCE.randomStringFromDecimalNumbers(sizeSpecification.getRandomSizeAccordingToSpecification());
        for (UnaryOperator<String> transformation : transformations) {
            result = transformation.apply(result);
        }
        return result;
    }
}
