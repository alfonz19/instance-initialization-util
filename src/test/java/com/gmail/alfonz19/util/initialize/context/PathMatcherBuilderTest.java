package com.gmail.alfonz19.util.initialize.context;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Slf4j
public class PathMatcherBuilderTest {

    @Test
    public void testInput() {
        assertPattern(".", PathMatcherBuilder.root().getPattern(), true);
    }

    @Test
    public void t2() {
        assertPattern("..", PathMatcherBuilder.root().getPattern(), false);
    }

    @Test
    public void t3() {
        assertPattern(".abc", PathMatcherBuilder.root().getPattern(), false);
    }

    @Test
    public void t4() {
        assertPattern(".[1]", PathMatcherBuilder.root().addArrayIndex(1).getPattern(), true);
    }

    @Test
    public void t5() {
        assertPattern(".[1]", PathMatcherBuilder.root().addArrayIndex(2).getPattern(), false);
    }

    @Test
    public void t6() {
        assertPattern(".[\"1\"]", PathMatcherBuilder.root().addArrayIndex(1).getPattern(), false);
    }

    @Test
    public void t7() {
        assertPattern(".[\"1\"]", PathMatcherBuilder.root().addAssociativeArrayKey("1").getPattern(), true);
    }

    @Test
    public void t8() {
        assertPattern(".[1]", PathMatcherBuilder.root().addAssociativeArrayKey("1").getPattern(), false);
    }

    @Test
    public void t9() {
        assertPattern(".abc", PathMatcherBuilder.root().addProperty("abc").getPattern(), true);
    }

    @Test
    public void t10() {
        assertPattern(".abcde", PathMatcherBuilder.root().addProperty("abc").getPattern(), false);
    }

    @Test
    public void t11() {
        assertPattern(".abc.def", PathMatcherBuilder.root().addProperty("abc").addProperty("def").getPattern(), true);
    }

    @Test
    public void t12() {
        assertPattern(".abc[1]", PathMatcherBuilder.root().addProperty("abc").addArrayIndex(1).getPattern(), true);
    }

    @Test
    public void t13() {
        assertPattern(".abc[\"1\"]", PathMatcherBuilder.root().addProperty("abc").addAssociativeArrayKey("1").getPattern(), true);
    }

    @Test
    public void t14() {
        assertPattern(".abc[1].def", PathMatcherBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPattern(), true);
    }

    @Test
    public void t15() {
        assertPattern(".abc[1].def", PathMatcherBuilder.root().addAnyProperty().addArrayIndex(1).addProperty("def").getPattern(), true);
    }

    @Test
    public void t16() {
        assertPattern(".abc[1].def", PathMatcherBuilder.root().addProperty("abc").addArrayIndex(1).addAnyProperty().getPattern(), true);
    }

    @Test
    public void t17() {
        assertPattern(".abc[1].def", PathMatcherBuilder.root().addProperty("abc").addAnyArrayIndex().addAnyProperty().getPattern(), true);
    }

    @Test
    public void t18() {
        assertPattern(".abc[1].def", PathMatcherBuilder.root().addAnyPathComponent().addAnyArrayIndex().addAnyProperty().getPattern(), true);
    }

    @Test
    public void t19() {
        assertPattern(".abc", PathMatcherBuilder.root().addAnyPathComponent().getPattern(), true);
    }

    @Test
    public void t20() {
        assertPattern(".[0]", PathMatcherBuilder.root().addAnyPathComponent().getPattern(), true);
    }

    @Test
    public void t21() {
        assertPattern(".[\"0\"]", PathMatcherBuilder.root().addAnyPathComponent().getPattern(), true);
    }

    @Test
    public void t22() {
        assertPattern(".abc[1].def", PathMatcherBuilder.root().addAnyPathComponent().addAnyPathComponent().addAnyPathComponent().getPattern(), true);
    }

    @Test
    public void t23() {
        assertPattern(".abc[1].def", PathMatcherBuilder.root().addAnySubPath().addAnyPathComponent().addAnyPathComponent().getPattern(), true);
    }

    @Test
    public void t24() {
        assertPattern(".abc[1].def", PathMatcherBuilder.root().addAnySubPath().addAnyPathComponent().getPattern(), true);
    }

    @Test
    public void t25() {
        assertPattern(".abc[1].def", PathMatcherBuilder.root().addAnySubPath().getPattern(), true);
    }

    private void assertPattern(String input, Pattern pattern, boolean matches) {
        log.debug("Testing pattern: {}", pattern);
        assertThat(String.format("Expected pattern %s to %s input %s", pattern, (matches?" match ": " not match "), input), pattern.matcher(input).matches(), is(matches));

    }

//    @Test
//    public void t2() {
//        PathMatcherBuilder.root().getPattern().matcher(".abc[1].def").matches()
//        PathMatcherBuilder.root().getPattern().matcher(".abc.def[1].def").matches()
//    }
}