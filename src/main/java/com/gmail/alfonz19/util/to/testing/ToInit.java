package com.gmail.alfonz19.util.to.testing;

import java.util.List;

//@Data //TODO MMUCHA: lombok
public class ToInit {
    int initWithConstant;
    int initRandomly;
    int initRandomly2;
    int initFromRange;
    String someStringValue;
    List<AssociatedClass> associatedClassList;
    List<List<AssociatedClass>> doubleList;

    public int getInitWithConstant() {
        return initWithConstant;
    }

    public void setInitWithConstant(int initWithConstant) {
        this.initWithConstant = initWithConstant;
    }

    public int getInitRandomly() {
        return initRandomly;
    }

    public void setInitRandomly(int initRandomly) {
        this.initRandomly = initRandomly;
    }

    public int getInitRandomly2() {
        return initRandomly2;
    }

    public void setInitRandomly2(int initRandomly2) {
        this.initRandomly2 = initRandomly2;
    }

    public int getInitFromRange() {
        return initFromRange;
    }

    public void setInitFromRange(int initFromRange) {
        this.initFromRange = initFromRange;
    }

    public String getSomeStringValue() {
        return someStringValue;
    }

    public void setSomeStringValue(String someStringValue) {
        this.someStringValue = someStringValue;
    }

    public List<AssociatedClass> getAssociatedClassList() {
        return associatedClassList;
    }

    public void setAssociatedClassList(List<AssociatedClass> associatedClassList) {
        this.associatedClassList = associatedClassList;
    }

    public List<List<AssociatedClass>> getDoubleList() {
        return doubleList;
    }

    public void setDoubleList(List<List<AssociatedClass>> doubleList) {
        this.doubleList = doubleList;
    }
}
