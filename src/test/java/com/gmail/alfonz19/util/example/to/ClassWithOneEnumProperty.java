package com.gmail.alfonz19.util.example.to;

import lombok.Data;

@Data
public class ClassWithOneEnumProperty {
    private E enumProperty;

    public enum E {
        A,B
    }
}
