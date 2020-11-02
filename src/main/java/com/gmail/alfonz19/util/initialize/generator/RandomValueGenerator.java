package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.path.PathNode;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;
import com.gmail.alfonz19.util.initialize.util.ReflectUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@SuppressWarnings({"squid:S119"})//type variables
public class RandomValueGenerator extends AbstractGenerator<Object> {

    private GuessTypeResult guessTypeResultToReuse;

    private final boolean useDefaultValueAsFallback;
    private final boolean reusingGuessedType;

    public RandomValueGenerator(boolean useDefaultValueAsFallback, boolean reusingGuessedType) {
        this.useDefaultValueAsFallback = useDefaultValueAsFallback;
        this.reusingGuessedType = reusingGuessedType;
    }

    private GuessTypeResult guessType(PropertyDescriptor propertyDescriptor, CalculatedNodeData calculatedNodeData) {
        Type substitutedType =
                ReflectUtil.substituteTypeVariables(propertyDescriptor, calculatedNodeData.getTypeVariableAssignment());

        return guessType(substitutedType);
    }

    private GuessTypeResult guessType(Type substitutedType) {
        if (substitutedType instanceof Class<?>) {
            return new GuessTypeResult((Class<?>) substitutedType, supportsType((Class<?>) substitutedType));
        } else {
            return GuessTypeResult.cannotGenerate();
        }
    }

    private GuessTypeResult guessType(PathNode pathNode) {
        return guessType(pathNode.getCalculatedNodeData().getGenericClassType());
    }

    private boolean supportsType(Class<?> classType) {
        if (classType.isEnum()) {
            return true;
        }

        List<Class<?>> supportedClasses = Arrays.asList(
                java.lang.String.class,
                java.util.UUID.class,
                java.util.Date.class,
                java.time.OffsetDateTime.class,
                java.sql.Timestamp.class,
                java.lang.Boolean.class,
                Boolean.TYPE,
                Integer.TYPE,
                java.lang.Integer.class,
                Long.TYPE,
                java.lang.Long.class,
                java.lang.Double.class,
                java.math.BigDecimal.class);

        return supportedClasses.contains(classType);
    }

    public boolean canGenerateValue(PropertyDescriptor propertyDescriptor, CalculatedNodeData calculatedNodeData) {
        if (reusingGuessedType) {
            if (guessTypeResultToReuse == null) {
                guessTypeResultToReuse = guessType(propertyDescriptor, calculatedNodeData);
            } else {
                throw new IllegalStateException("Already initialized, you're probably trying to reuse RandomValueGenerator for multiple properties, which is unsupported");
            }

            return useDefaultValueAsFallback || guessTypeResultToReuse.canGenerate;
        } else {
            return useDefaultValueAsFallback || guessType(propertyDescriptor, calculatedNodeData).canGenerate;

        }
    }

    @Override
    public Object create(PathNode pathNode) {
        if (reusingGuessedType) {
            if (guessTypeResultToReuse == null) {
                guessTypeResultToReuse = guessType(pathNode);
            }

            if (guessTypeResultToReuse.canGenerate) {
                return randomValueFor(guessTypeResultToReuse.willGenerate);
            } else {
                if (useDefaultValueAsFallback) {
                    return ReflectUtil.nullOrDefaultValue(pathNode.getCalculatedNodeData().getClassType());
                } else {
                    throw new InitializeException("Cannot create random value, unsupported type.");
                }
            }
        } else {
            GuessTypeResult guessTypeResult = guessType(pathNode);
            if (guessTypeResult.canGenerate) {
                return randomValueFor(guessTypeResult.willGenerate);
            } else {
                if (useDefaultValueAsFallback) {
                    return ReflectUtil.nullOrDefaultValue(pathNode.getCalculatedNodeData().getClassType());
                } else {
                    throw new InitializeException("Cannot create random value, unsupported type.");
                }
            }

        }
    }

    ////TODO MMUCHA: extension point via service loader or registration.
    private static Object randomValueFor(Class<?> instanceClassType) {
        if (instanceClassType.isEnum()) {
            return RandomUtil.INSTANCE.randomEnum(instanceClassType);
        }

        switch (instanceClassType.getName()) {
            case "java.lang.String":
                return RandomUtil.INSTANCE.randomStringFromDecimalNumbers(10);
            case "java.util.UUID":
                return UUID.randomUUID();
            case "java.util.Date":
                int date = RandomUtil.INSTANCE.getRandom().nextInt();
                date = date < 0 ? -1 * date : date;
                return new Date(date);
            case "java.time.OffsetDateTime":
                return OffsetDateTime.ofInstant(Instant.ofEpochMilli(RandomUtil.INSTANCE.getRandom().nextInt()),
                        ZoneId.ofOffset("GMT", ZoneOffset.ofHours(1)));
            case "java.sql.Timestamp":
                int date2 = RandomUtil.INSTANCE.getRandom().nextInt();
                date2 = date2 < 0 ? -1 * date2 : date2;
                return new Timestamp(date2);
            case "java.lang.Boolean":
            case "boolean":
                return RandomUtil.INSTANCE.getRandom().nextBoolean();
            case "int":
            case "java.lang.Integer":
                return RandomUtil.INSTANCE.getRandom().nextInt();
            case "long":
            case "java.lang.Long":
                return RandomUtil.INSTANCE.getRandom().nextLong();
            case "java.lang.Double":
                return RandomUtil.INSTANCE.getRandom().nextDouble();
            case "java.math.BigDecimal":
                return BigDecimal.valueOf(RandomUtil.INSTANCE.getRandom().nextDouble());

            default:
                throw new InitializeException("Coding error, trying to initialize unsupported class type: "+instanceClassType);
        }
    }

    @AllArgsConstructor
    private static class GuessTypeResult {
        public final Class<?> willGenerate;
        public final boolean canGenerate;

        public static GuessTypeResult cannotGenerate() {
            return new GuessTypeResult(null, false);
        }
    }
}