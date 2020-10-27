package com.gmail.alfonz19.util.example.to;

import java.util.function.Supplier;

public enum EnumImplementingInterface implements Supplier<String> {
    A,
    B;

    public String get() {
        return name();
    }
}
