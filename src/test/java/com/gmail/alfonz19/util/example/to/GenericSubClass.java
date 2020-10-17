package com.gmail.alfonz19.util.example.to;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class GenericSubClass<X extends Number, K> extends GenericClass<X>{
    private List<X> tlist;
    private K k;
}
