package com.gmail.alfonz19.util.example.to;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class RootDto {
    private int someIntegerProperty;
    private Enumerated enumerated;
    private int someIntegerProperty2;
    private String someStringValue;
    private String initializedStringValue = "initialized";
    private Integer initializedIntegerValue = 123;

    @SuppressWarnings("squid:S1170")
    private final String initializedFinalStringValue = "initialized";
    private List<AssociatedClass> listOfAssociatedClasses;
    private List<List<AssociatedClass>> listOfListsOfAssociatedClasses;
    private List<TestingInterface> listInterfaces;
    private RootDto anotherRootDtoToInit;
    private Map<List<AssociatedClass>, Set<TestingInterface>> complicatedMap;

    public enum Enumerated {
        A,B,C
    }
}
