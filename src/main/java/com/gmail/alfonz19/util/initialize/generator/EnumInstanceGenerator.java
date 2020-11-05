package com.gmail.alfonz19.util.initialize.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.InitializationConfiguration;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings({"squid:S119"})//type variables
//we cannot limit ENUM_CLASS to extend Enum<? extends ENUM_CLASS> because we need to be generate enum and return it as a interface this enum implements.
//and generics syntax requires first item in extends to be class and then interfaces. If first is interface latter cannot be class.
public class EnumInstanceGenerator<ENUM_CLASS extends GENERATES, GENERATES> extends AbstractGenerator<GENERATES> {
    private final Class<ENUM_CLASS> testEnumClass;
    private List<ENUM_CLASS> selectFrom;

    public EnumInstanceGenerator(Class<ENUM_CLASS> enumClass, @SuppressWarnings("unused") Class<GENERATES> generates) {
        if (!Objects.requireNonNull(enumClass).isEnum()) {
            throw new IllegalArgumentException("parameter enumClass must be Enum descendant");
        }

        this.testEnumClass = enumClass;
        random();
        setCalculatedNodeData(true, new CalculatedNodeData(enumClass));
    }

    //TODO MMUCHA: not sure why this variant should be any useful.
    public EnumInstanceGenerator(Class<ENUM_CLASS> enumClass, @SuppressWarnings("unused") TypeReference<GENERATES> generates) {
        if (!Objects.requireNonNull(enumClass).isEnum()) {
            throw new IllegalArgumentException("parameter enumClass must be Enum descendant");
        }

        this.testEnumClass = enumClass;
        random();
        setCalculatedNodeData(true, new CalculatedNodeData(enumClass));
    }

    public EnumInstanceGenerator() {
        testEnumClass = null;
    }

    //TODO MMUCHA: verify Generates can be assigned from enum?
    @Override
    public GENERATES create(InitializationConfiguration initializationConfiguration, PathNode pathNode) {
        if (selectFrom == null) {
            if (testEnumClass == null) {
                //enum instance wasn't known earlier, try to find it out from pathNode

                Class<?> classType = pathNode.getCalculatedNodeData().getClassType();
                if (!classType.isEnum()) {
                    throw new InitializeException("Trying to create enum while in pathNode there isn't enum value.");
                }

                //noinspection unchecked
                selectFrom = (List<ENUM_CLASS>) Arrays.asList(classType.getEnumConstants());

                if (!classType.isEnum()) {
                    throw new InitializeException("Trying to create enum while in pathNode there isn't enum value.");
                }
            } else {
                //should not happen, enum instance is know, but random wasn't call. But why not, lets call it here for completeness.
                random();
            }

            return RandomUtil.INSTANCE.randomFromList(selectFrom);
        } else if (selectFrom.isEmpty()) {
            return null;
        } else {
            return RandomUtil.INSTANCE.randomFromList(selectFrom);
        }
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
