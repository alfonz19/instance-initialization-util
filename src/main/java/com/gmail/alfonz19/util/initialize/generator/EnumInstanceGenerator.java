package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.PathNode;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class EnumInstanceGenerator<ENUM_CLASS, GENERATES> extends AbstractGenerator<GENERATES> {
    private final Class<ENUM_CLASS> testEnumClass;
    private List<ENUM_CLASS> selectFrom;

    public EnumInstanceGenerator(Class<ENUM_CLASS> testEnumClass, Class<? extends GENERATES> generates) {
        this.testEnumClass = testEnumClass;
        random();
        setCalculatedNodeData(true, new CalculatedNodeData(testEnumClass));
    }

    @Override
    public GENERATES create(PathNode pathNode) {
        //TODO MMUCHA: this will produce runtime exception, try to fix but with kept possibility to assign enumerated type into iterfaces.
        return (GENERATES) RandomUtil.INSTANCE.randomFromList(selectFrom);
    }

    public EnumInstanceGenerator<ENUM_CLASS, GENERATES> random() {
        this.selectFrom = Arrays.asList(testEnumClass.getEnumConstants());
        return this;
    }

    @SafeVarargs
    public final EnumInstanceGenerator<ENUM_CLASS, GENERATES> randomFrom(ENUM_CLASS... values) {
        selectFrom = Arrays.asList(values);
        return this;
    }
}
