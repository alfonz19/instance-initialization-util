package com.gmail.alfonz19.util.to.testing;

import lombok.Data;

import java.util.List;

@Data
public class ToInit {
    int initWithConstant;
    int initRandomly;
    int initRandomly2;
    int initFromRange;
    String someStringValue;
    List<AssociatedClass> associatedClassList;
    List<List<AssociatedClass>> doubleList;
}
