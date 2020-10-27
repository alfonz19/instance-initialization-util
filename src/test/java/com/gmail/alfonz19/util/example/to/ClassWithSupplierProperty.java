package com.gmail.alfonz19.util.example.to;

import lombok.Data;

import java.util.function.Supplier;

@Data
public class ClassWithSupplierProperty {
    Supplier<String> supplier;
}
