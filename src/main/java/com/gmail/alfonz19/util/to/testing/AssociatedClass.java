package com.gmail.alfonz19.util.to.testing;

//@Data //TODO MMUCHA: lombok
public class AssociatedClass {
    int a;
    int b;
    ToInit toInit;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public ToInit getToInit() {
        return toInit;
    }

    public void setToInit(ToInit toInit) {
        this.toInit = toInit;
    }
}
