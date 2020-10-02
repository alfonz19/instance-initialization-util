package com.gmail.alfonz19.util.initialize.context;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
//TODO MMUCHA: rename.
@Slf4j
public class PathMatcherBuilderTest {

    @Test
    public void t1() {
        assertPattern(PathBuilder.root().getPath(),
                PathMatcherBuilder.root().build(),
                true);
    }

    @Test
    public void t2() {
        assertPattern("..",
                PathMatcherBuilder.root().build(),
                false);
    }

    @Test
    public void t3() {
        assertPattern(PathBuilder.root().addProperty("abc").getPath(),
                PathMatcherBuilder.root().build(),
                false);
    }

    @Test
    public void t4() {
        assertPattern(PathBuilder.root().addArrayIndex(1).getPath(),
                PathMatcherBuilder.root().addArrayIndex(1).build(),
                true);
    }

    @Test
    public void t5() {
        assertPattern(PathBuilder.root().addArrayIndex(1).getPath(),
                PathMatcherBuilder.root().addArrayIndex(2).build(),
                false);
    }

    @Test
    public void t6() {
        assertPattern(PathBuilder.root().addAssociativeArrayKey("1").getPath(),
                PathMatcherBuilder.root().addArrayIndex(1).build(),
                false);
    }

    @Test
    public void t7() {
        assertPattern(PathBuilder.root().addAssociativeArrayKey("1").getPath(),
                PathMatcherBuilder.root().addAssociativeArrayKey("1").build(),
                true);
    }

    @Test
    public void t8() {
        assertPattern(PathBuilder.root().addArrayIndex(1).getPath(),
                PathMatcherBuilder.root().addAssociativeArrayKey("1").build(),
                false);
    }

    @Test
    public void t9() {
        assertPattern(PathBuilder.root().addProperty("abc").getPath(),
                PathMatcherBuilder.root().addProperty("abc").build(),
                true);
    }

    @Test
    public void t10() {
        assertPattern(PathBuilder.root().addProperty("abcde").getPath(),
                PathMatcherBuilder.root().addProperty("abc").build(),
                false);
    }

    @Test
    public void t11() {
        assertPattern(PathBuilder.root().addProperty("abc").addProperty("def").getPath(),
                PathMatcherBuilder.root().addProperty("abc").addProperty("def").build(),
                true);
    }

    @Test
    public void t12() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).getPath(),
                PathMatcherBuilder.root().addProperty("abc").addArrayIndex(1).build(),
                true);
    }

    @Test
    public void t13() {
        assertPattern(PathBuilder.root().addProperty("abc").addAssociativeArrayKey("1").getPath(),
                PathMatcherBuilder.root().addProperty("abc").addAssociativeArrayKey("1").build(),
                true);
    }

    @Test
    public void t14() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").build(),
                true);
    }

    @Test
    public void t15() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addAnyProperty().addArrayIndex(1).addProperty("def").build(),
                true);
    }

    @Test
    public void t15_2() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addOptionalProperty().addArrayIndex(1).addProperty("def").build(),
                true);
    }

    @Test
    public void t15_3() {
        assertPattern(PathBuilder.root().addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addOptionalProperty().addArrayIndex(1).addProperty("def").build(),
                true);
    }

    @Test
    public void t16() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addProperty("abc").addArrayIndex(1).addAnyProperty().build(),
                true);
    }

    @Test
    public void t17() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addProperty("abc").addAnyArrayIndex().addAnyProperty().build(),
                true);
    }

    @Test
    public void t18() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addAnyPathComponent().addAnyArrayIndex().addAnyProperty().build(),
                true);
    }

    @Test
    public void t19() {
        assertPattern(PathBuilder.root().addProperty("abc").getPath(),
                PathMatcherBuilder.root().addAnyPathComponent().build(),
                true);
    }

    @Test
    public void t20() {
        assertPattern(PathBuilder.root().addArrayIndex(0).getPath(),
                PathMatcherBuilder.root().addAnyPathComponent().build(),
                true);
    }

    @Test
    public void t21() {
        assertPattern(PathBuilder.root().addAssociativeArrayKey("0").getPath(),
                PathMatcherBuilder.root().addAnyPathComponent().build(),
                true);
    }

    @Test
    public void t22() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root()
                        .addAnyPathComponent()
                        .addAnyPathComponent()
                        .addAnyPathComponent()
                        .build(),
                true);
    }

    @Test
    public void t23() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addAnySubPath().addAnyPathComponent().addAnyPathComponent().build(),
                true);
    }

    @Test
    public void t24() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addAnySubPath().addAnyPathComponent().build(),
                true);
    }

    @Test
    public void t25() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addAnySubPath().build(),
                true);
    }

    @Test
    public void t26() {
        assertPattern(PathBuilder.root().addProperty("whatever").getPath(),
                PathMatcherBuilder.root().addAnySubPath().addAnyProperty().build(),
                true);
    }

    //-------------------

    @Test
    public void t27() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addAnyOptionalPathComponent().addAnyArrayIndex().addAnyProperty().build(),
                true);
    }

    @Test
    public void t27_2() {
        assertPattern(PathBuilder.root().addArrayIndex(1).getPath(),
                PathMatcherBuilder.root().addAnyOptionalPathComponent().addAnyArrayIndex().addAnyOptionalPathComponent().build(),
                true);
    }

    @Test
    public void t28() {
        assertPattern(PathBuilder.root().addProperty("abc").getPath(),
                PathMatcherBuilder.root().addAnyOptionalPathComponent().build(),
                true);
    }

    @Test
    public void t28_2() {
        assertPattern(PathBuilder.root().getPath(),
                PathMatcherBuilder.root().addAnyOptionalPathComponent().build(),
                true);
    }

    @Test
    public void t29() {
        assertPattern(PathBuilder.root().addArrayIndex(0).getPath(),
                PathMatcherBuilder.root().addAnyOptionalPathComponent().build(),
                true);
    }

    @Test
    public void t29_2() {
        assertPattern(PathBuilder.root().getPath(),
                PathMatcherBuilder.root().addAnyOptionalPathComponent().build(),
                true);
    }

    @Test
    public void t30() {
        assertPattern(PathBuilder.root().addAssociativeArrayKey("0").getPath(),
                PathMatcherBuilder.root().addAnyOptionalPathComponent().build(),
                true);
    }

    @Test
    public void t30_2() {
        assertPattern(PathBuilder.root().getPath(),
                PathMatcherBuilder.root().addAnyOptionalPathComponent().build(),
                true);
    }

    @Test
    public void t31() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root()
                        .addAnyOptionalPathComponent()
                        .addAnyOptionalPathComponent()
                        .addAnyOptionalPathComponent()
                        .build(),
                true);
    }

    @Test
    public void t31_2() {
        assertPattern(PathBuilder.root().getPath(),
                PathMatcherBuilder.root()
                        .addAnyOptionalPathComponent()
                        .addAnyOptionalPathComponent()
                        .addAnyOptionalPathComponent()
                        .build(),
                true);
    }

    private void assertPattern(String input, PathMatcher pathMatcher, boolean matches) {
        assertThat(pathMatcher.matches(input), is(matches));

    }
}