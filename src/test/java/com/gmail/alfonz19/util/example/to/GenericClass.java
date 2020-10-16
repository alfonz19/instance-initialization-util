package com.gmail.alfonz19.util.example.to;

import lombok.Data;

@Data
public class GenericClass<T extends Number> {
    private T t;
}
