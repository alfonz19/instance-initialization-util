package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import lombok.Data;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static com.gmail.alfonz19.util.initialize.SettingListWithContextViaPropertyWhenShufflingIsTurnedOffTest.TestInstance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.list;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class SettingListWithContextViaPropertyWhenShufflingIsTurnedOffTest extends AbstractTestSingleAndMultipleInstanceCreation<TestInstance>{

    //the number must be bigger to avoid randomly sorted values.
    public static final int SIZE_OF_LIST = 50;
    private final boolean shufflingEnabled;

    @Parameterized.Parameters(name = "with shuffling enabled: {0}")
    public static Object[] getParameters() {
        return new Object[]{
                true,
                false
        };
    }

    public SettingListWithContextViaPropertyWhenShufflingIsTurnedOffTest(boolean shufflingEnabled) {
        this.shufflingEnabled = shufflingEnabled;
    }

    @Override
    protected Generator<TestInstance> createGenerator() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        return instance(TestInstance::new)
                .setPropertyTo(
                        TestInstance::getAList,
                        list(instance(A::new)
                                .setPropertyTo(A::getContext,
                                        Generators.constantString(null).updatedWithContext((a, b) -> b.getPath().getPathAsString()))
                                .setPropertyTo(A::getValue, path -> atomicInteger.getAndIncrement())
                        ).shuffled(shufflingEnabled)
                                .withSize(SIZE_OF_LIST)
                );
    }

    @Override
    protected void assertCreatedInstance(TestInstance instance) {
//        System.out.println(instance);

        assertThat(instance, notNullValue());

        List<A> aList = instance.getAList();
        assertThat(aList, notNullValue());
        assertThat(aList.size(), is(SIZE_OF_LIST));

        for (int i = 0; i < aList.size(); i++) {
            A a = aList.get(i);
            assertThat(a.getContext(), is(String.format(".AList[%d].context", i)));
        }

        boolean valuesInIncrementingOrder = true;
        int previousValue = aList.get(0).getValue();
        for (int i = 1; i < aList.size(); i++) {
            valuesInIncrementingOrder = valuesInIncrementingOrder && (previousValue + 1 == aList.get(i).getValue());
            previousValue = aList.get(i).getValue();
        }

        assertThat("Collection values should "+(shufflingEnabled?"not":"")+" be in incrementing order", valuesInIncrementingOrder, is(!shufflingEnabled));
    }

    @Data
    public static class TestInstance {
        private List<A> aList;

        @Override
        public String toString() {
            return "TestInstance {\n" + "\taList=" + aList + "\n" + '}';
        }
    }

    @Data
    public static class A {
        private String context;
        private int value;

        @Override
        public String toString() {
            return "{\n" + "\t\tcontext='" + context + '\'' + ",\n\t\tvalue=" + value + "\n\t}";
        }
    }
}