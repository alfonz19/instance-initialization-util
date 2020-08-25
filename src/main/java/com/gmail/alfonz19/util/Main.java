package com.gmail.alfonz19.util;

import com.gmail.alfonz19.util.attempt2.CollectionConfiguration2;
import com.gmail.alfonz19.util.attempt2.CollectionItemsConfiguration;
import com.gmail.alfonz19.util.to.testing.AssociatedClass;
import com.gmail.alfonz19.util.to.testing.ToInit;

import java.util.ArrayList;
import java.util.List;

//type variables, unused method parameters, unused constructs, unused result of method call
@SuppressWarnings({"squid:S119", "squid:S1172", "unused", "ResultOfMethodCallIgnored"})
public class Main {

    public static void main(String[] args) {

        ToInit initialized = Initialize.instance(ToInit::new)
                .initRandomly(ToInit::getInitRandomly, ToInit::getInitRandomly2)
                .initRandomlyFromRange(ToInit::getInitFromRange, 1, 5)
                .initRandomlyFromRange(ToInit::getInitFromRange, 1, 5)
//                .initRandomlyFromRange(ToInit::getSomeStringValue, 1, 5) invalid!
                .setValue(ToInit::getSomeStringValue, "abc")

                .initCollection(AssociatedClass.class, ArrayList::new, ToInit::getAssociatedClassList).usingItemSupplier(index->new AssociatedClass()).toSize(3)
                .initCollection(AssociatedClass.class, ArrayList::new, ToInit::getAssociatedClassList).usingItemSupplier(AssociatedClass::new).toSize(3, 10)

                .initRandomly(ToInit::getSomeStringValue)

                .initCollection(AssociatedClass.class, ArrayList::new, ToInit::getAssociatedClassList).usingItemSupplier(AssociatedClass::new)
                .withEachItem()
                .initRandomly(AssociatedClass::getA)
                .create()
                .toSize(3)

//                .initCollection(List.class, ArrayList::new,ToInit::getDoubleList).usingItemSupplier((Supplier<List<AssociatedClass>>) LinkedList::new).toSize(5)


//                .initCollection(itemType, ArrayList::new ,ToInit::getDoubleList).toSize(3)


//                .initCollection(List.class, ArrayList::new, ToInit::getDoubleList)
//                    .usingItemSupplier((Supplier<List<AssociatedClass>>) ArrayList::new)
//                    .toSize(5)

                //a nuisance given by type reuse & self-bounding generics. When returning from builder to parent builder, we can be only it's most generic type, which
                //needs to be turned into actual type by this call or by any method call of method defined on builder abstract class.
                .getSelf()
                .create();


        List<ToInit> collection = Initialize.collection(ToInit.class, ArrayList::new).usingItemSupplier(ToInit::new).toSize(5);


        CollectionConfiguration2<ArrayList<I>> s1 = Initialize.list(ArrayList::new);


        CollectionItemsConfiguration<A> s2 = s1.usingItemSupplier(A::new);

        List<I> i = s2.withEachItem().beingNull();


//        TODO    supplier dodává zdroj dat. kolekce může mít buď jednoho, anebo více náhodně či pořadím volených supplierů. Supplier určuje, co to bude za instanci a tedy
//                jak se bude inicializovat. Conditional supplier, random supplier

        System.out.println(initialized);
        System.out.println(collection);
    }

    public static interface I {
        void test();
    }

    public static class A implements I {
        @Override
        public void test() {
        }
    }

    public static class B implements I {
        @Override
        public void test() {
        }
    }

}
