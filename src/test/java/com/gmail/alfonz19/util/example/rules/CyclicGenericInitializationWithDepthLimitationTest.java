package com.gmail.alfonz19.util.example.rules;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gmail.alfonz19.testsupport.InitializedInstanceTestLogging;
import com.gmail.alfonz19.util.example.to.GenericSubClass;
import com.gmail.alfonz19.util.initialize.rules.Rules;
import lombok.Data;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static com.gmail.alfonz19.util.initialize.Initializer.configureRules;
import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static com.gmail.alfonz19.util.initialize.rules.RuleBuilder.applyGenerator;

public class CyclicGenericInitializationWithDepthLimitationTest {

    @Rule
    public InitializedInstanceTestLogging initializedInstanceLogger = new InitializedInstanceTestLogging();

    //TODO MMUCHA: implement and uncomment.
    @Ignore
    @Test
    public void crazyCyclicGenericInitializationWithDepthLimitation() {
        Rules rules = new Rules()
                .addRule(applyGenerator(instance(new TypeReference<GenericSubClass<Integer, GenericSubClassUsedInDepths.A>>() {}))
                        .ifClassTypeIsEqualTo(GenericSubClass.class));

        GenericSubClassUsedInDepths instance =
                configureRules(rules).andCreate(instance(GenericSubClassUsedInDepths.class));
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
