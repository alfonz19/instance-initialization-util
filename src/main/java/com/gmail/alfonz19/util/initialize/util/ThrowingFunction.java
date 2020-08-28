package com.gmail.alfonz19.util.initialize.util;

import com.gmail.alfonz19.util.initialize.exception.InitializeException;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Just like {@link Function} prescribes transformation from `T` to `R`, this
 * {@link ThrowingFunction} does the same, only the method `apply` here prescribes some exception (namely checked one)
 * in the method signature. But note, that this can be used even for not-checked exceptions.
 *
 * <p>This class then contains several static methods which can be used in stream `map` method for example. As streams does
 * not allow checked exceptions in it, we need to consume them, before they can reach stream api. This static methods
 * takes {@link ThrowingFunction}, invokes it, and since it does not declare checked exception in its header, it must
 * have been somehow handled.
 *
 * <p>This is not strictly related to checked exceptions, one can use it for RuntimeExceptions as well:
 *
 * <pre>
 * {@code
 *      Stream.of("invalid", UUID.randomUUID().toString())
 *              .map(ThrowingFunction.wrapInOptional(UUID::fromString))
 *              .map(e -> e.map(UUID::toString).orElse("failed to parse!"))
 *              .forEach(System.out::println);}
 * </pre>
 *
 * which will then produce 2 lines: "failed to parse!" and "7725aece-0d1d-46d5-a5a5-08a18a259117".
 *
 * Invocation outside of stream is little bit cumbersome, and probably pointless. But possible, like this:
 *
 * <pre>
 * {@code
 *         ThrowingFunction<String, UUID, Exception> convert = UUID::fromString;
 *         UUID output = ThrowingFunction.wrapInOptional(convert)
 *                 .apply("incorrect!").orElse(null);}
 * </pre>
 *
 * @param <T>
 * @param <R>
 * @param <E>
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {
    R apply(T t) throws E;

    static <T, R, E extends Exception> Function<T, R> wrapInInitializeException(ThrowingFunction<T, R, E> f) {
        return t -> {
            try {
                return f.apply(t);
            } catch (Exception e) {
                throw new InitializeException(e);
            }
        };
    }

    static <T, R, E extends Exception> Function<T, R> wrapInInitializeException(ThrowingFunction<T, R, E> f, String message) {
        return t -> {
            try {
                return f.apply(t);
            } catch (Exception e) {
                throw new InitializeException(message, e);
            }
        };
    }

    static <T, R, E extends Exception> Function<T, R> wrapInException(ThrowingFunction<T, R, E> f,
                                                                      Function<Exception, ? extends RuntimeException> wrappingExceptionSupplier) {
        return t -> {
            try {
                return f.apply(t);
            } catch (Exception e) {
                throw wrappingExceptionSupplier.apply(e);
            }
        };
    }

    static Function<Exception, RuntimeException> asInitializeExceptionWithMessageWrappingFunction(String message) {
        return e -> new InitializeException(message, e);
    }

    static <T, R, E extends Exception> Function<T, Optional<R>> wrapInOptional(ThrowingFunction<T, R, E> f) {
        return wrapInOptional(f, e -> {});
    }

    static <T, R, E extends Exception> Function<T, Optional<R>> wrapInOptional(ThrowingFunction<T, R, E> f,
                                                                               Consumer<Exception> swallowedExceptionConsumer) {
        return t -> {
            try {
                return Optional.of(f.apply(t));
            } catch (Exception e) {
                swallowedExceptionConsumer.accept(e);
                return Optional.empty();
            }
        };
    }

    static <T, R, E extends Exception> Function<T, R> returnNullForError(ThrowingFunction<T, R, E> f) {
        return returnDefaultForError(f, null, e-> {});
    }

    static <T, R, E extends Exception> Function<T, R> returnDefaultForError(ThrowingFunction<T, R, E> f,
                                                                            R defaultValue,
                                                                            Consumer<Exception> swallowedExceptionConsumer) {
        return t -> {
            try {
                return f.apply(t);
            } catch (Exception e) {
                swallowedExceptionConsumer.accept(e);
                return defaultValue;
            }
        };
    }
}
