package com.gmail.alfonz19.util.initialize;

import com.gmail.alfonz19.testsupport.InitializedInstanceTestLogging;
import lombok.Data;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static com.gmail.alfonz19.util.initialize.Initialize.initialize;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

public class SkippingInitializationTest {
    @Rule
    public InitializedInstanceTestLogging initializedInstanceLogger = new InitializedInstanceTestLogging();

    @Test
    public void name() {
        TestInstance instance = initialize(instance(TestInstance.class)
                .skipProperty(TestInstance::getB)
                .setUnsetPropertiesRandomly()
        );

        initializedInstanceLogger.logInitializedInstance(instance);

        Assert.assertThat(instance.getA(), notNullValue());
        Assert.assertThat(instance.getB(), nullValue());
        Assert.assertThat(instance.getC(), notNullValue());
    }

    @Data
    public static class TestInstance {
        private String a;
        private String b;
        private String c;
    }
}
