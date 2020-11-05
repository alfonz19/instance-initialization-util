package com.gmail.alfonz19.util.example.rules;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gmail.alfonz19.testsupport.InitializedInstanceTestLogging;
import com.gmail.alfonz19.util.example.to.GenericSubClass;
import com.gmail.alfonz19.util.initialize.rules.Rules;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;

import static com.gmail.alfonz19.util.initialize.Initializer.configureRules;
import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.list;
import static com.gmail.alfonz19.util.initialize.generator.Generators.randomForGuessedType;
import static com.gmail.alfonz19.util.initialize.rules.RuleBuilder.applyGenerator;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class InitializationOfGenericClassHavingGenericParentTest {

    @Rule
    public InitializedInstanceTestLogging initializedInstanceLogger = new InitializedInstanceTestLogging();

    /**
     * Creates generic GenericSubClass using given types `<Integer, String>`, automatically initializing any
     * List property with 5 items. In this case the List contains items of generic type.
     */
    @Test
    public void automaticInitializationOfGenericClassHavingGenericParent() {
        Rules rules = new Rules()
                .addRule(applyGenerator(list(randomForGuessedType(true, false)).withSize(5))
                        .ifClassTypeIsEqualTo(List.class));

        GenericSubClass<Integer, String> instance = configureRules(rules)
                .andCreate(instance(new TypeReference<GenericSubClass<Integer, String>>() {})
                        .setUnsetPropertiesRandomly());

        initializedInstanceLogger.logInitializedInstance(instance);


        assertThat(instance, isA(GenericSubClass.class));
        assertThat(instance.getTlist(), notNullValue());
        assertThat(instance.getTlist().size(), CoreMatchers.is(5));

        instance.getTlist().forEach(e -> {
                    assertThat(e, isA(Integer.class));
                    assertThat(e, notNullValue());
                }
        );

        assertThat(instance.getK(), notNullValue());
        assertThat(instance.getK(), isA(String.class));


        assertThat(instance.getT(), notNullValue());
    }
}
