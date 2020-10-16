package com.gmail.alfonz19.util.example.to;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GenericSubClass<T extends Number, K> extends GenericClass<T>{
    private List<T> tlist;
    private K k;
}
