package com.gmail.alfonz19.util.initialize.rules;

import com.gmail.alfonz19.util.initialize.Config;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import com.gmail.alfonz19.util.initialize.util.ClassDataCache;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;
import com.gmail.alfonz19.util.initialize.util.ReflectUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gmail.alfonz19.util.initialize.Config.UNCONFIGURED_STRING_SIZE;
import static com.gmail.alfonz19.util.initialize.generator.Generators.enumeratedType;
import static com.gmail.alfonz19.util.initialize.generator.Generators.generatorFromSupplier;
import static com.gmail.alfonz19.util.initialize.generator.Generators.list;
import static com.gmail.alfonz19.util.initialize.generator.Generators.randomForGuessedType;
import static com.gmail.alfonz19.util.initialize.generator.Generators.randomString;
import static com.gmail.alfonz19.util.initialize.generator.Generators.set;
import static com.gmail.alfonz19.util.initialize.generator.PathNodePredicates.classTypeIsEqualTo;
import static com.gmail.alfonz19.util.initialize.rules.RuleBuilder.applyGenerator;
import static com.gmail.alfonz19.util.initialize.rules.RuleBuilder.createNewGeneratorAndApply;
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
                .toCreateRule("Automatically create random string consisting of numeric values for java.lang.String types")
                .ifClassTypeIsEqualTo(String.class);
    }

    public static RuleBuilder uuidType4() {
        return applyGenerator(generatorFromSupplier(UUID::randomUUID))
                .toCreateRule("Automatically create UUIDv4 for java.util.UUID types")
                .ifClassTypeIsEqualTo(UUID.class);
    }

    public static RuleBuilder integerValue() {
        return applyGenerator(Generators.randomInt())
                .toCreateRule("Automatically create random int value for java.lang.Integer type and primitive int")
                .addTest(or(classTypeIsEqualTo(Integer.TYPE), classTypeIsEqualTo(Integer.class)));
    }

    public static RuleBuilder longValue() {
        return applyGenerator(Generators.randomLong())
                .toCreateRule("Automatically create random long value for java.lang.Long type and primitive long")
                .addTest(or(classTypeIsEqualTo(Long.TYPE), classTypeIsEqualTo(Long.class)));
    }

    public static RuleBuilder shortValue() {
        return applyGenerator(Generators.randomShort())
                .toCreateRule("Automatically create random short value for java.lang.Short type and primitive short")
                .addTest(or(classTypeIsEqualTo(Short.TYPE), classTypeIsEqualTo(Short.class)));
    }

    public static RuleBuilder byteValue() {
        return applyGenerator(Generators.randomByte())
                .toCreateRule("Automatically create random byte value for java.lang.Byte type and primitive byte")
                .addTest(or(classTypeIsEqualTo(Byte.TYPE), classTypeIsEqualTo(Byte.class)));
    }

    public static RuleBuilder booleanValue() {
        return applyGenerator(Generators.generatorFromSupplier(()->RandomUtil.INSTANCE.getRandom().nextBoolean()))
                .toCreateRule("Automatically create random boolean value for java.lang.Boolean type and primitive boolean")
                .addTest(or(classTypeIsEqualTo(Boolean.TYPE), classTypeIsEqualTo(Boolean.class)));
    }

    public static RuleBuilder enumeratedValue() {
        return applyGenerator(enumeratedType())
                .toCreateRule("Automatically create random enumerated value for java.lang.Enum type")
                .ifClassTypeIsAssignableFrom(Enum.class);
    }

    public static RuleBuilder createInstanceUsingNoArgConstructor() {
        return createNewGeneratorAndApply(Generators::newInstanceViaNoArgConstructor)
                .toCreateRule("Automatically create new instance for null valued properties, which class does have zero-arg constructor and path ins't too deep")
                //instance == null happens if we don't have instance available when applying rule. Typically can happen in collections, where items are generated prior to collection instance, as not all can be created first (like Stream).
                .ifPathNode("property is null-valued", (instance, pathNode) -> instance != null && pathNode.getCurrentValue(instance) == null)
                .ifType("has public no-arg constructor", classType -> ClassDataCache.canBeInstantiatedUsingNoArgConstructor(ReflectUtil.getRawType(classType)))
                .ifPathLengthIsLessThan(Config.MAX_DEPTH_FOR_AUTOMATIC_NO_ARG_CONSTRUCTOR_INSTANCE_CREATION);
    }

    public static RuleBuilder createListInstances() {
        return RuleBuilder.applyGenerator(list(randomForGuessedType(true)))
                .toCreateRule("Automatically create new instance for java.util.List descendants")
                //instance == null happens if we don't have instance available when applying rule. Typically can happen in collections, where items are generated prior to collection instance, as not all can be created first (like Stream).
                .ifPathNode((instance, pathNode) -> instance != null && pathNode.getCurrentValue(instance) == null)
                .ifClassTypeIsAssignableFrom(List.class)
                .ifPathLengthIsLessThan(Config.MAX_DEPTH_FOR_AUTOMATIC_NO_ARG_CONSTRUCTOR_INSTANCE_CREATION);
    }

    public static RuleBuilder createSetInstances() {
        return RuleBuilder.applyGenerator(set(randomForGuessedType(true)))
                .toCreateRule("Automatically create new instance for java.util.Set descendants")
                //instance == null happens if we don't have instance available when applying rule. Typically can happen in collections, where items are generated prior to collection instance, as not all can be created first (like Stream).
                .ifPathNode((instance, pathNode) -> instance != null && pathNode.getCurrentValue(instance) == null)
                .ifClassTypeIsAssignableFrom(Set.class)
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
