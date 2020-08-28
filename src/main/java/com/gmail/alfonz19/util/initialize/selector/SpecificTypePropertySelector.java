package com.gmail.alfonz19.util.initialize.selector;

@FunctionalInterface
public interface SpecificTypePropertySelector<T, K> {
    K select(T t);
}
