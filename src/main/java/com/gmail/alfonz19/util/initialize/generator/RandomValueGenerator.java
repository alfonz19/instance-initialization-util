package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.builder.PathContext;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import com.gmail.alfonz19.util.initialize.util.RandomUtil;

import java.beans.PropertyDescriptor;
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

@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
public class RandomValueGenerator implements Generator<Object> {


    @Override
    public Object create(PathContext pathContext) {
        Class<?> instanceClassType = pathContext.getCalculatedNodeData().getInstanceClassType();
        return randomValueFor(instanceClassType);
    }

    public boolean canGenerateValueFor(PropertyDescriptor propertyDescriptor) {
        //TODO MMUCHA: duplicate with com.gmail.alfonz19.util.initialize.builder.InstanceConfiguration.PropertyDescriptorInitialization figuring out classType should be in 1 place.
        Class<?> classType = propertyDescriptor.getPropertyType();
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
                throw new InitializeException("Coding error, trying to initialize unsupported class type.");
        }
    }
}