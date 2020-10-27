package com.gmail.alfonz19.util.example.to;

import lombok.Data;

@Data
public class ClassImplementingInterfaceA implements TestingInterface {
    @Override
    public void test() {
    }

    private String stringProperty;

}
