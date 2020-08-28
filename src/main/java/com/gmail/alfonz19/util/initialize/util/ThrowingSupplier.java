package com.gmail.alfonz19.util.initialize.util;

import com.gmail.alfonz19.util.initialize.exception.InitializeException;

import java.util.function.Function;
import java.util.function.Supplier;

//S1181: Throwable shouldn't be used, but lot of libraries use it, thus so have to us.
//unused: this is library, it's ok, if something isn't used.
@SuppressWarnings({"squid:S1181", "unused"})
@FunctionalInterface
public interface ThrowingSupplier<T, E extends Throwable> {
    T get() throws E;

    static <T, E extends Throwable> Supplier<T> wrapInInitializeException(ThrowingSupplier<T, E> f) {
        return () -> {
            try {
                return f.get();
            } catch (Throwable e) {
                throw new InitializeException(e);
            }
        };
    }

    static <T, E extends Throwable> Supplier<T> wrapInInitializeException(ThrowingSupplier<T, E> f, String message) {
        return () -> {
            try {
                return f.get();
            } catch (Throwable e) {
                throw new InitializeException(message, e);
            }
        };
    }

    static <T, E extends Throwable> Supplier<T> wrapInException(ThrowingSupplier<T, E> f,
                                                                      Function<Throwable, ? extends RuntimeException> wrappingExceptionSupplier) {
        return () -> {
            try {
                return f.get();
            } catch (Throwable e) {
                throw wrappingExceptionSupplier.apply(e);
            }
        };
    }
}
