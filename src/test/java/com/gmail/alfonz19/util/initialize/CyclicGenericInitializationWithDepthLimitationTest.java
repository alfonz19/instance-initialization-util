package com.gmail.alfonz19.util.initialize;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gmail.alfonz19.testsupport.InitializedInstanceLogger;
import com.gmail.alfonz19.util.example.to.GenericSubClass;
import com.gmail.alfonz19.util.initialize.generator.Rules;
import lombok.Data;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static com.gmail.alfonz19.util.initialize.generator.InitializationUsingRules.withConfiguration;
import static com.gmail.alfonz19.util.initialize.generator.RuleBuilder.applyGenerator;

public class CyclicGenericInitializationWithDepthLimitationTest {

    @Rule
    public InitializedInstanceLogger initializedInstanceLogger = new InitializedInstanceLogger();

    @Ignore
    @Test
    public void crazyCyclicGenericInitializationWithDepthLimitation() {
        Rules rules = new Rules()
                .addRule(applyGenerator(instance(new TypeReference<GenericSubClass<Integer, GenericSubClassUsedInDepths.A>>() {}))
                        .ifPropertyClassTypeIsEqualTo(GenericSubClass.class));

        GenericSubClassUsedInDepths instance = withConfiguration(rules)
                .initialize(instance(GenericSubClassUsedInDepths.class));
        initializedInstanceLogger.logInitializedInstance(instance);
    }

    @Data
    public static class GenericSubClassUsedInDepths {
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
}
