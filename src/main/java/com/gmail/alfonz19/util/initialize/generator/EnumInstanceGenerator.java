package com.gmail.alfonz19.util.initialize.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.PathNode;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"squid:S119"})//type variables
//we cannot limit ENUM_CLASS to extend Enum<? extends ENUM_CLASS> because we need to be generate enum and return it as a interface this enum implements.
//and generics syntax requires first item in extends to be class and then interfaces. If first is interface latter cannot be class.
public class EnumInstanceGenerator<ENUM_CLASS extends GENERATES, GENERATES> extends AbstractGenerator<GENERATES> {
    private final Class<ENUM_CLASS> testEnumClass;
    private List<ENUM_CLASS> selectFrom;

    public EnumInstanceGenerator(Class<ENUM_CLASS> enumClass, Class<GENERATES> generates) {
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException("parameter enumClass must be Enum descendant");
        }

        this.testEnumClass = enumClass;
        random();
        setCalculatedNodeData(true, new CalculatedNodeData(enumClass));
    }

    public EnumInstanceGenerator(Class<ENUM_CLASS> enumClass, TypeReference<GENERATES> generates) {
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException("parameter enumClass must be Enum descendant");
        }

        this.testEnumClass = enumClass;
        random();
        setCalculatedNodeData(true, new CalculatedNodeData(enumClass));
    }

    @Override
    public GENERATES create(PathNode pathNode) {
        return RandomUtil.INSTANCE.randomFromList(selectFrom);
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
