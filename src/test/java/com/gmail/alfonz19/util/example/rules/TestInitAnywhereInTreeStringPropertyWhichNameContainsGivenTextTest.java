package com.gmail.alfonz19.util.example.rules;

import com.gmail.alfonz19.testsupport.InitializedInstanceTestLogging;
import com.gmail.alfonz19.util.initialize.context.path.matcher.PathMatcherBuilder;
import com.gmail.alfonz19.util.initialize.generator.Generators;
import com.gmail.alfonz19.util.initialize.rules.Rules;
import lombok.Data;

import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;

import static com.gmail.alfonz19.util.initialize.Initialize.withConfiguration;
import static com.gmail.alfonz19.util.initialize.generator.Generators.instance;
import static com.gmail.alfonz19.util.initialize.generator.Generators.list;
import static com.gmail.alfonz19.util.initialize.rules.RuleBuilder.applyGenerator;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class TestInitAnywhereInTreeStringPropertyWhichNameContainsGivenTextTest {

    @Rule
    public InitializedInstanceTestLogging initializedInstanceLogger = new InitializedInstanceTestLogging();

    @Test
    public void initAnywhereInTreeStringPropertyWhichNameContainsGivenText() {
        TestInstance testInstance = withConfiguration(new Rules()
                .addRule(applyGenerator(Generators.randomString().withSize(10).addPrefix("success"))
                        .ifClassTypeIsEqualTo(String.class)
                        .ifPathMatches(PathMatcherBuilder.root().addAnySubPath().addPropertyRegex("thisShouldBeSet.*?"))
                ))
                .initialize(instance(TestInstance.class)
                        .setProperty(TestInstance::getAssociatedInstances).to(list(instance(TestInstance::new)).withSize(2)));

        initializedInstanceLogger.logInitializedInstance(testInstance);
        assertTestInstance(testInstance);

        List<TestInstance> associatedInstances = testInstance.getAssociatedInstances();
        assertThat(associatedInstances, notNullValue());
        assertThat(associatedInstances.size(), is(2));
        associatedInstances.forEach(e -> {
            assertTestInstance(e);
            assertThat(e.getAssociatedInstances(), nullValue());
        });
    }

    private void assertTestInstance(TestInstance testInstance) {
        assertThat(testInstance.getUnrelated(), is(0));
        assertThat(testInstance.getUnrelated2(), nullValue());
        assertThat(testInstance.getUnrelated3(), notNullValue());
        assertThat(testInstance.getUnrelated4(), nullValue());
        assertThat(testInstance.getUnrelatedEnum(), nullValue());

        assertThat(testInstance.getThisShouldBeSetAsItMatchesRule(), notNullValue());
        assertThat(testInstance.getThisShouldBeSetAsItMatchesRule().length(), is(17));
        assertThat(testInstance.getThisShouldBeSetAsItMatchesRule(), CoreMatchers.startsWith("success"));
        assertThat(testInstance.getThisShouldBeSetAsItMatchesRuleAlso(), notNullValue());
        assertThat(testInstance.getThisShouldBeSetAsItMatchesRuleAlso().length(), is(17));
        assertThat(testInstance.getThisShouldBeSetAsItMatchesRuleAlso(), CoreMatchers.startsWith("success"));

        assertThat(testInstance.getButNotThis(), is("originalValue"));
    }

    @Data
    public static class TestInstance {
        private int unrelated;
        private Integer unrelated2;
        private Date unrelated3 = new Date();
        private Date unrelated4;
        private UnrelatedEnum unrelatedEnum;
        private List<TestInstance> associatedInstances;
        private String thisShouldBeSetAsItMatchesRule;
        private String thisShouldBeSetAsItMatchesRuleAlso = "originalValue";
        private String butNotThis = "originalValue";
    }

    public enum UnrelatedEnum {
        UNRELATED
    }
}
