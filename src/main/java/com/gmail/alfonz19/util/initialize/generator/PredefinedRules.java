package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.Config;
import com.gmail.alfonz19.util.initialize.context.Rule;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import com.gmail.alfonz19.util.initialize.util.ClassDataCache;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;
import com.gmail.alfonz19.util.initialize.util.ReflectUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gmail.alfonz19.util.initialize.Config.UNCONFIGURED_STRING_SIZE;
import static com.gmail.alfonz19.util.initialize.generator.Generators.enumeratedType;
import static com.gmail.alfonz19.util.initialize.generator.Generators.generatorFromSupplier;
import static com.gmail.alfonz19.util.initialize.generator.Generators.randomString;
import static com.gmail.alfonz19.util.initialize.generator.RuleBuilder.applyGenerator;
import static com.gmail.alfonz19.util.initialize.generator.PathNodePredicates.classTypeIsEqualTo;
import static com.gmail.alfonz19.util.initialize.generator.RuleBuilder.createNewGeneratorAndApply;
import static com.gmail.alfonz19.util.initialize.util.PredicatesBooleanOperations.or;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PredefinedRules {

    public static List<Rule> ALL_RULES = createAllRulesFromThisClass();

    private static List<Rule> createAllRulesFromThisClass() {
        return Arrays.stream(PredefinedRules.class.getMethods())
                .filter(method->method.getReturnType().equals(RuleBuilder.class))
                .filter(method->method.getParameterCount() == 0)
                .map(method -> {
                    try {
                        return (RuleBuilder)method.invoke(null);
                    } catch (Exception e) {
                        throw new InitializeException("Unable to create list of all predefined rules", e);
                    }
                })
                .map(RuleBuilder::build)
                .collect(Collectors.toList());
    }

    public static RuleBuilder string() {
        return applyGenerator(randomString().withSize(UNCONFIGURED_STRING_SIZE))
                .ifClassTypeIsEqualTo(String.class);
    }

    public static RuleBuilder uuidType4() {
        return applyGenerator(generatorFromSupplier(UUID::randomUUID))
                .ifClassTypeIsEqualTo(UUID.class);
    }

    public static RuleBuilder integerValue() {
        return applyGenerator(Generators.randomInt())
                .addTest(or(classTypeIsEqualTo(Integer.TYPE), classTypeIsEqualTo(Integer.class)));
    }

    public static RuleBuilder longValue() {
        return applyGenerator(Generators.randomLong())
                .addTest(or(classTypeIsEqualTo(Long.TYPE), classTypeIsEqualTo(Long.class)));
    }

    public static RuleBuilder shortValue() {
        return applyGenerator(Generators.randomShort())
                .addTest(or(classTypeIsEqualTo(Short.TYPE), classTypeIsEqualTo(Short.class)));
    }

    public static RuleBuilder byteValue() {
        return applyGenerator(Generators.randomByte())
                .addTest(or(classTypeIsEqualTo(Byte.TYPE), classTypeIsEqualTo(Byte.class)));
    }

    public static RuleBuilder booleanValue() {
        return applyGenerator(Generators.generatorFromSupplier(()->RandomUtil.INSTANCE.getRandom().nextBoolean()))
                .addTest(or(classTypeIsEqualTo(Boolean.TYPE), classTypeIsEqualTo(Boolean.class)));
    }

    public static RuleBuilder enumeratedValue() {
        return applyGenerator(enumeratedType()).ifClassTypeIsAssignableFrom(Enum.class);
    }

    public static RuleBuilder createInstanceUsingNoArgConstructor() {
        return createNewGeneratorAndApply(Generators::newInstanceViaNoArgConstructor)

                //instance == null happens if we don't have instance available when applying rule. Typically can happen in collections, where items are generated prior to collection instance, as not all can be created first (like Stream).
                .ifPathNode((instance, pathNode) -> instance != null && pathNode.getCurrentValue(instance) == null)
                .ifType(classType -> ClassDataCache.canBeInstantiatedUsingNoArgConstructor(ReflectUtil.getRawType(classType)))
                .ifPathLengthIsLessThan(Config.MAX_DEPTH_FOR_AUTOMATIC_NO_ARG_CONSTRUCTOR_INSTANCE_CREATION);
    }

//    java.util.Date.class,
//    java.time.OffsetDateTime.class,
//    java.sql.Timestamp.class,
//    other java 8 time

//    java.lang.Double.class,
//    float
//    java.math.BigDecimal.class);


}
