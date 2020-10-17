package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.context.PathNode;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
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
@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class RandomValueGenerator extends AbstractGenerator<Object> {

    private final Class<?> willGenerate;
    private final boolean canGenerate;

    public RandomValueGenerator(PropertyDescriptor propertyDescriptor,
                                CalculatedNodeData calculatedNodeData) {
        Class<?> classType = propertyDescriptor.getPropertyType();

        Type genericReturnType = propertyDescriptor.getReadMethod().getGenericReturnType();
        boolean justAClass = genericReturnType instanceof Class<?>;
        if (justAClass) {
            this.willGenerate = classType;
            this.canGenerate = supportsType(willGenerate);
            return;
        }

        boolean isTypeVariable = genericReturnType instanceof TypeVariable;
        if (isTypeVariable) {
            if (calculatedNodeData.getTypeVariableAssignment().isEmpty()) {
                log.debug("Unable to instantiate generic type {} because I have no info about actual implementation.", genericReturnType);
                this.canGenerate = false;
                this.willGenerate = null;
            } else {
                Type type = calculatedNodeData.getTypeVariableAssignment().get(genericReturnType);
                if (type instanceof Class) {
                    this.willGenerate = (Class<?>)type;
                    this.canGenerate = supportsType(willGenerate);
                } else {
                    log.debug("Unable to instantiate generic type {} after substitution, because I have no idea what it is.", genericReturnType);
                    this.canGenerate = false;
                    this.willGenerate = null;
                }
            }
        } else {
            log.debug("I have no idea what this class ({}) is, cannot instantiate.", classType);
            this.canGenerate = false;
            this.willGenerate = null;
        }
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

    @Override
    public Object create(PathNode pathNode) {
        return randomValueFor(willGenerate);
    }

    public boolean canGenerateValue() {
        return canGenerate;
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
}