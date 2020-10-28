package com.gmail.alfonz19.util.example.to;

import lombok.Data;

@Data
public class ClassReferencingGenericSubSubClass {
    private GenericSubSubClass<Integer> subSubClass;
}
