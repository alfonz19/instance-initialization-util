package com.gmail.alfonz19.util.example.to;

import lombok.Data;

@Data
public class GenericSubClassUsedInDepths {
    private A a;

    @Data
    public static class A {
        private B b;

        @Data
        public static class B {
            private GenericSubClass<Integer, A> gsc;
        }
    }
}
