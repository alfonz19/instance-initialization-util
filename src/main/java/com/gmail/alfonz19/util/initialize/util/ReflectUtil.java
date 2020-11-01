package com.gmail.alfonz19.util.initialize.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gmail.alfonz19.util.initialize.context.CalculatedNodeData;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.gmail.alfonz19.util.initialize.util.TypeVariableAssignments.NO_TYPE_VARIABLE_ASSIGNMENTS;

@Slf4j
@SuppressWarnings({"squid:S119", "squid:S1172", "unused"})//type variables, unused method parameters, unused constructs.
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectUtil {
    private static final Map<Class<?>, Object> DEFAULT_VALUES = createDefaultValues();

    private static Map<Class<?>, Object> createDefaultValues() {
        Map<Class<?>, Object> map = new HashMap<>();
        map.put(boolean.class, false);
        map.put(char.class, '\0');
        map.put(byte.class, (byte) 0);
        map.put(short.class, (short) 0);
        map.put(int.class, 0);
        map.put(long.class, 0L);
        map.put(float.class, 0f);
        map.put(double.class, 0d);
        return Collections.unmodifiableMap(map);
    }

    public static Object nullOrDefaultValue(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            return DEFAULT_VALUES.get(returnType);
        } else {
            return null;
        }
    }

    public static <T> Supplier<T> supplierFromClass(Class<T> instance) {
        return () -> {
            try {
                return instance.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new InitializeException(e);
            }
        };
    }

    /**
     * @return Array of {@code arrayType} containing all given {@code items}.
     */
    public static <ITEM_TYPE> ITEM_TYPE[] createArray(Class<ITEM_TYPE> arrayType, Collection<? extends ITEM_TYPE> items) {
        Object array = Array.newInstance(arrayType, items.size());
        //noinspection unchecked    //there is not way how to do this in type-safe way.
        ITEM_TYPE[] castedArray = (ITEM_TYPE[]) array;
        return items.toArray(castedArray);
    }

    public static <T> Class<T> classTypeOfItemsProducedBySupplier(Supplier<? extends T> instanceSupplier) {
        //noinspection unchecked
        return (Class<T>) instanceSupplier.get().getClass();
    }

    public static <T> Class<T> replaceClassTypeIfItIsInterface(Class<T> sourceInstanceClass,
                                                               Supplier<? extends T> instanceSupplier) {
        return sourceInstanceClass.isInterface()
                ? classTypeOfItemsProducedBySupplier(instanceSupplier)
                : sourceInstanceClass;
    }

    public static Type substituteTypeVariables(PropertyDescriptor propertyDescriptor, TypeVariableAssignments typeVariableAssignments) {
        Method readMethod = propertyDescriptor.getReadMethod();
        Class<?> declaringClass = readMethod.getDeclaringClass();

        Type genericReturnType = readMethod.getGenericReturnType();
        boolean justAClass = genericReturnType instanceof Class<?>;
        if (justAClass) {
            return propertyDescriptor.getPropertyType();
        }

        boolean isParameterizedType = genericReturnType instanceof ParameterizedType;
        if (isParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

            Type[] substitutedTypes = Arrays.stream(actualTypeArguments)
                    .map(type -> {
                        if (type instanceof TypeVariable) {
                            TypeVariable<?> typeVariable = (TypeVariable<?>) type;
                            if (! typeVariableAssignments.containsAssignmentInfoFor(declaringClass, typeVariable)) {
                                log.error(String.format("Unable to substitute type %s as it's not among know type variables", type));
                                return typeVariable;
                            }
                            return typeVariableAssignments.getAssignmentFor(declaringClass, typeVariable);
                        } else {
                            return type;
                        }
                    }).toArray(Type[]::new);

            //TODO MMUCHA: can we cast it like this here?
            return createParameterizedType((Class<?>) parameterizedType.getRawType(), substitutedTypes);
        }

        boolean isTypeVariable = genericReturnType instanceof TypeVariable;
        if (isTypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) genericReturnType;
            if (!typeVariableAssignments.containsAssignmentInfoFor(declaringClass, typeVariable)) {
                log.error(String.format("Unable to substitute type %s as it's not among know type variables", genericReturnType));
                return typeVariable;
            }
            return typeVariableAssignments.getAssignmentFor(declaringClass, typeVariable);
        }


        log.error("Unable to substitute type variables, unimplemented branch.");
        return null;
    }

    //TODO MMUCHA: missing recalculation.
    public static TypeVariableAssignments recalculateTypeVariableAssignment(PropertyDescriptor propertyDescriptor,
                                                                            TypeVariableAssignments typeVariableAssignment) {
        return NO_TYPE_VARIABLE_ASSIGNMENTS;
    }

    /**
     * Creates a parameterized type for the specified raw type and actual type arguments.
     *
     * @param rawType the raw type
     * @param actualTypeArguments the actual type arguments
     *
     * @return the parameterized type
     *
     * @throws MalformedParameterizedTypeException if the number of actual type arguments differs from those defined on the raw type
     */
    public static ParameterizedType createParameterizedType(final Class<?> rawType, final Type... actualTypeArguments) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return actualTypeArguments;
            }

            @Override
            public Type getRawType() {
                return rawType;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public String toString() {
                return rawType+"<"+Arrays.stream(actualTypeArguments).map(Type::getTypeName).collect(Collectors.joining(","))+">";
            }
        };
    }

    public static <SOURCE_INSTANCE> TypeVariableAssignments typeVariableAssignment(Class<SOURCE_INSTANCE> sourceInstanceClass,
                                                                                   TypeReference<SOURCE_INSTANCE> typeReference) {
        return typeVariableAssignment(sourceInstanceClass, typeReference.getType());
    }

    public static <SOURCE_INSTANCE> TypeVariableAssignments getTypeVariableAssignment(Class<SOURCE_INSTANCE> sourceInstanceClass) {
        return typeVariableAssignment(sourceInstanceClass, sourceInstanceClass);
    }

    public static <SOURCE_INSTANCE> TypeVariableAssignments typeVariableAssignment(Class<SOURCE_INSTANCE> sourceInstanceClass,
                                                                                   Type type) {
        boolean notPossibleToFindOutAnyTypeInformation = type instanceof Class<?>;
        if (notPossibleToFindOutAnyTypeInformation) {
            return NO_TYPE_VARIABLE_ASSIGNMENTS;
        }

        Map<Class<?>, Map<TypeVariable<?>, Type>> typeVariableAssignmentPerClass = new HashMap<>();

        TypeVariable<Class<SOURCE_INSTANCE>>[] typeParameters = sourceInstanceClass.getTypeParameters();
        Type[] actualTypeArguments = getActualTypeArguments(type);

        Map<TypeVariable<?>, Type> genericTypePairing = createTypeParameterToActualParameterPairing(typeParameters,
                actualTypeArguments,
                Collections.emptyMap());

        typeVariableAssignmentPerClass.put(sourceInstanceClass, genericTypePairing);

        //now walk over parents to fill up whole hierarchy of known generic types
        type = sourceInstanceClass.getGenericSuperclass();
        Map<TypeVariable<?>, Type> lastPairing = genericTypePairing;
        while (type instanceof ParameterizedType) {
            Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
            TypeVariable<? extends Class<?>>[] tp = rawType.getTypeParameters();
            Type[] ata = ((ParameterizedType) type).getActualTypeArguments();

            lastPairing = createTypeParameterToActualParameterPairing(tp, ata, lastPairing);
            typeVariableAssignmentPerClass.put(rawType, lastPairing);


            type = rawType.getGenericSuperclass();
        }


        return new TypeVariableAssignments(typeVariableAssignmentPerClass);
    }

    private static Map<TypeVariable<?>, Type> createTypeParameterToActualParameterPairing(TypeVariable<? extends Class<?>>[] typeParameters,
                                                                                          Type[] actualTypeArguments,
                                                                                          Map<TypeVariable<?>, Type> parentPairing) {
        int typeParametersLength = typeParameters.length;
        int actualTypeArgumenstLength = actualTypeArguments.length;
        if (typeParametersLength != actualTypeArgumenstLength) {
            throw new IllegalArgumentException("Weird state, lenght of type parameters and it's actual values differs");
        }

        Map<TypeVariable<?>, Type> pairing = new HashMap<>(typeParametersLength);
        for(int i = 0; i < typeParametersLength; i++) {
            Type actualTypeArgument = actualTypeArguments[i];
            if (actualTypeArgument instanceof TypeVariable) {
                pairing.put(typeParameters[i], parentPairing.get(actualTypeArgument));
            } else {
                pairing.put(typeParameters[i], actualTypeArgument);
            }
        }
        return pairing;
    }

    public static CalculatedNodeData unwrapParameterizedType(CalculatedNodeData calculatedNodeData) {
        if (!calculatedNodeData.representsParameterizedType()) {
            throw new IllegalArgumentException("Trying to unwrap parameterized type, where given calculatedNodeData does not represent parameterized type. " +
                    "This is most likely to happen when we're trying to figure out calculatedNodeData for items in collection when there aren't enough" +
                    "of data to do so.");
        }

        ParameterizedType parameterizedType = (ParameterizedType) calculatedNodeData.getGenericClassType();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments.length != 1) {
            throw new IllegalArgumentException("Cannot unwrap parameterized type which does not have exactly 1 actual parameter");
        }

        Type actualTypeArgument = actualTypeArguments[0];
        Class<?> rawType = getRawType(actualTypeArgument);
        return new CalculatedNodeData(rawType, actualTypeArgument, calculatedNodeData.getTypeVariableAssignment());

    }

    public static <T> Class<T> getRawType(Type typeOfTypeReference) {
        if (typeOfTypeReference instanceof Class) {
            //noinspection unchecked
            return (Class<T>) typeOfTypeReference;
        }

        ParameterizedType type = (ParameterizedType) typeOfTypeReference;
        Type rawType = type.getRawType();
        if (!(rawType instanceof Class)) {
            throw new IllegalArgumentException("Cannot create supplier, raw type isn't class.");
        }
        //noinspection unchecked
        return (Class<T>) rawType;
    }

    public static Type[] getActualTypeArguments(Type typeOfTypeReference) {
        if (typeOfTypeReference instanceof Class) {
            return new Type[0];
        }

        return ((ParameterizedType) typeOfTypeReference).getActualTypeArguments();
    }

    public static <T> T callNewInstance(Constructor<T> constructor) {
        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new InitializeException("Cannot create new instance");
        }
    }
}
