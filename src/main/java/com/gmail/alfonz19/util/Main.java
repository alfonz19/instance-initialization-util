package com.gmail.alfonz19.util;

import com.gmail.alfonz19.util.to.testing.AssociatedClass;
import com.gmail.alfonz19.util.to.testing.ToInit;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {

        ToInit initialized = InitializeConfiguration.initializeToCreate(ToInit::new)
                .initRandomly(ToInit::getInitRandomly, ToInit::getInitRandomly2)
                .initRandomlyFromRange(ToInit::getInitFromRange, 1, 5)
                .initRandomlyFromRange(ToInit::getInitFromRange, 1, 5)
//                .initRandomlyFromRange(ToInit::getSomeStringValue, 1, 5) invalid!
                .set(ToInit::getSomeStringValue, "abc")

                .initCollection(ToInit::getAssociatedClassList).usingItemSupplier(index->new AssociatedClass()).toSize(3)
                .initCollection(ToInit::getAssociatedClassList).usingItemSupplier(AssociatedClass::new).toSize(3, 10)

                .initCollection(ToInit::getDoubleList).usingItemSupplier((Supplier<List<AssociatedClass>>) LinkedList::new).toSize(5)

                .create();

        System.out.println(initialized);
    }

}
