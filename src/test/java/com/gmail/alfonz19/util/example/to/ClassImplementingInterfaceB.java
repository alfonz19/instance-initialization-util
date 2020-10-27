package com.gmail.alfonz19.util.example.to;

import lombok.Data;

@Data
public class ClassImplementingInterfaceB implements TestingInterface {
    @Override
    public void test() {
    }

    private Integer integerProperty;
}
