package com.gmail.alfonz19.util.example.rules;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gmail.alfonz19.testsupport.InitializedInstanceTestLogging;
import com.gmail.alfonz19.util.initialize.Initialize;
import com.gmail.alfonz19.util.initialize.rules.Rules;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static com.gmail.alfonz19.util.initialize.generator.Generators.list;
import static com.gmail.alfonz19.util.initialize.generator.Generators.randomForGuessedType;
import static com.gmail.alfonz19.util.initialize.rules.RuleBuilder.applyGenerator;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class InitializationListUsingGenericRuleTest {

    @Rule
    public InitializedInstanceTestLogging initializedInstanceLogger = new InitializedInstanceTestLogging();

    /**
     * Initialization using rule. Rule will create any List property it encounters with 5 items generated automatically.
     * Using this rule we will initialize `List<List<String>>`. Ie. produces List of 5 items, where each item is list
     * and have 5 items, where each is randomly initialized String.
     */
    @Test
    public void automaticInitializationListUsingGenericRule() {
        Rules rules = new Rules()
                .addRule(applyGenerator(list(randomForGuessedType(true, false)).withSize(5))
                        .ifClassTypeIsEqualTo(List.class));

        List<List<String>> instance =
                Initialize.withConfiguration(rules)
                        .initialize(list(LinkedList::new, new TypeReference<List<List<String>>>() {}));
        initializedInstanceLogger.logInitializedInstance(instance);

        assertCreatedInstance(instance, LinkedList.class);
    }

    @Test
    public void automaticInitializationListUsingGenericRule2() {
        Rules rules = new Rules()
                .addRule(applyGenerator(list(randomForGuessedType(true, false)).withSize(5))
                        .ifClassTypeIsEqualTo(List.class));

        List<List<String>> instance =
                Initialize.withConfiguration(rules)
                        .initialize(list(new TypeReference<List<List<String>>>() {}));
        initializedInstanceLogger.logInitializedInstance(instance);
        assertCreatedInstance(instance, ArrayList.class);
    }

    @Ignore //TODO MMUCHA: implement.
    @Test
    public void testInitializationGenericListUsingRule() {
//        Rules rules = new Rules()
//                .addRule(applyGenerator(list(randomForGuessedType(true, false)).withSize(5))
//                        .ifPropertyClassTypeIsEqualTo(List.class));
//
//        List<List<String>> instance =
//                InitializationUsingRules.withConfiguration(rules)
//                        .initialize(Generators.randomForGuessedType(true));
//        initializedInstanceLogger.logInitializedInstance(instance);
//        assertCreatedInstance(instance, ArrayList.class);
    }

    public void assertCreatedInstance(List<List<String>> instance, Class<?> topLevelInstanceClassType) {
        assertThat(instance, notNullValue());
        assertEquals(instance.getClass(), topLevelInstanceClassType);
        assertThat(instance.size(), is(5));
        instance.forEach(e->{
            assertThat(e, notNullValue());
            assertThat(e.size(), is(5));
            assertEquals(e.getClass(), ArrayList.class);
            e.forEach(f->{
                assertThat(f, notNullValue());
                assertEquals(f.getClass(), String.class);
            });
        });
    }
}
