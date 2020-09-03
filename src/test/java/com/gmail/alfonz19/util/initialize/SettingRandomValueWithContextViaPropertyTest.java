package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.util.initialize.generator.Generator;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import lombok.Data;

import java.util.List;

import static com.gmail.alfonz19.util.initialize.SettingRandomValueWithContextViaPropertyTest.TopLevelTestInstance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.list;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SettingRandomValueWithContextViaPropertyTest extends AbstractTestSingleAndMultipleInstanceCreation<TopLevelTestInstance>{

    @Override
    protected Generator<TopLevelTestInstance> createGenerator() {
        return instance(TopLevelTestInstance::new)
                .setPropertyTo(
                        TopLevelTestInstance::getAList,
                        list(instance(A::new).setPropertyTo(
                                A::getBList,
                                list(instance(B::new).setProperty(B::getText)
                                        .to(Generators.constantString("abc: ")
                                                .updatedWithContext((curr, context)->curr+context.getPath().getPathAsString())
                                        ))
                                .withSize(2)
                        ))
                        .withSize(3)
                );
    }

    @Override
    protected void assertCreatedInstance(TopLevelTestInstance instance) {
        assertThat(instance, notNullValue());

        List<A> aList = instance.getAList();
        assertThat(aList, notNullValue());
        assertThat(aList.size(), is(3));

        for (int i = 0; i < aList.size(); i++) {
            A a = aList.get(i);
            assertThat(a, notNullValue());
            List<B> bList = a.getBList();
            assertThat(bList, notNullValue());
            assertThat(bList.size(), is(2));
            for (int j = 0; j < bList.size(); j++) {
                B b = bList.get(j);
                assertThat(b, notNullValue());
//                assertThat(b.getText(), is("abc: "));
                assertThat(b.getText(), is(String.format("abc: .AList[%d].BList[%d].text", i, j)));
            }

        }
    }

    @Data
    public static class TopLevelTestInstance {
        private List<A> aList;
    }

    @Data
    public static class A {
        private List<B> bList;
    }

    @Data
    public static class B {
        String text;
    }
}