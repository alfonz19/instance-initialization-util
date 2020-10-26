package com.gmail.alfonz19.util.initialize;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gmail.alfonz19.testsupport.InitializedInstanceLogger;
import com.gmail.alfonz19.util.example.to.GenericSubClass;
import com.gmail.alfonz19.util.initialize.generator.InitializationUsingRules;
import com.gmail.alfonz19.util.initialize.generator.Rules;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.ClassRule;
import org.junit.Test;

import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.list;
import static com.gmail.alfonz19.util.initialize.generator.Generators.randomForGuessedType;
import static com.gmail.alfonz19.util.initialize.generator.RuleBuilder.applyGenerator;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class InitializationOfGenericClassHavingGenericParentTest {

    @ClassRule
    public static InitializedInstanceLogger initializedInstanceLogger = new InitializedInstanceLogger();

    /**
     * Creates generic GenericSubClass using given types `<Integer, String>`, automatically initializing any
     * List property with 5 items. In this case the List contains items of generic type.
     */
    @Test
    public void automaticInitializationOfGenericClassHavingGenericParent() {
        Rules rules = new Rules()
                .addRule(applyGenerator(list(randomForGuessedType(true, false)).withSize(5))
                        .ifPropertyClassTypeIsEqualTo(List.class));

        GenericSubClass<Integer, String> instance = InitializationUsingRules.withConfiguration(rules)

//                with provided instance supplier
//                instance(GenericSubClass::new, new TypeReference<GenericSubClass<Integer, String>>() {})
//                or with one created from TypeReference.

                .initialize(instance(new TypeReference<GenericSubClass<Integer, String>>() {})
                        .setUnsetPropertiesRandomlyUsingGuessedType());

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
