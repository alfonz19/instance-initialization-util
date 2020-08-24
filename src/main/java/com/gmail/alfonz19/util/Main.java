package com.gmail.alfonz19.util;

import com.gmail.alfonz19.util.to.testing.AssociatedClass;
import com.gmail.alfonz19.util.to.testing.ToInit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class Main {
//    public static <T> T list(){
//        return new ArrayList();
//    }

    public static void main(String[] args) {

//        List<AssociatedClass> itemType = new LinkedList<>();

        ToInit initialized = Initialize.instance(ToInit::new)
                .initRandomly(ToInit::getInitRandomly, ToInit::getInitRandomly2)
                .initRandomlyFromRange(ToInit::getInitFromRange, 1, 5)
                .initRandomlyFromRange(ToInit::getInitFromRange, 1, 5)
//                .initRandomlyFromRange(ToInit::getSomeStringValue, 1, 5) invalid!
                .setValue(ToInit::getSomeStringValue, "abc")

                .initCollection(AssociatedClass.class, ArrayList::new, ToInit::getAssociatedClassList).usingItemSupplier(index->new AssociatedClass()).toSize(3)
                .initCollection(AssociatedClass.class, ArrayList::new, ToInit::getAssociatedClassList).usingItemSupplier(AssociatedClass::new).toSize(3, 10)

                .initCollection(AssociatedClass.class, ArrayList::new, ToInit::getAssociatedClassList).usingItemSupplier(AssociatedClass::new)
                .withEachItem()
                .initRandomly(AssociatedClass::getA)

//                .initCollection(List.class, ArrayList::new,ToInit::getDoubleList).usingItemSupplier((Supplier<List<AssociatedClass>>) LinkedList::new).toSize(5)


//                .initCollection(itemType, ArrayList::new ,ToInit::getDoubleList).toSize(3)


//                .initCollection(List.class, ArrayList::new, ToInit::getDoubleList)
//                    .usingItemSupplier((Supplier<List<AssociatedClass>>) ArrayList::new)
//                    .toSize(5)

                .create();


        List<ToInit> collection = Initialize.collection(ToInit.class, ArrayList::new).usingItemSupplier(ToInit::new).toSize(5);

        TODO    supplier dodává zdroj dat. kolekce může mít buď jednoho, anebo více náhodně či pořadím volených supplierů. Supplier určuje, co to bude za instanci a tedy
                jak se bude inicializovat. Conditional supplier, random supplier


        /*List<ToInit> toInits = Initialize.<ToInit>list().usingItemSupplier(ToInit::new).toSize(3);


        CollectionConfiguration<ToInit> aa = Initialize.<ToInit>list().usingItemSupplier(ToInit::new);


        List<Object> objects = Initialize.list().usingItemSupplier(ToInit::new).toSize(3);*/

        System.out.println(initialized);
        System.out.println(collection);
    }

}
