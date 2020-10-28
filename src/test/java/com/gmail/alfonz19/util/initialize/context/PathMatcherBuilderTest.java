package com.gmail.alfonz19.util.initialize.context;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
@Slf4j
public class PathMatcherBuilderTest {

    @Test
    public void testMatchingRootPath() {
        assertPattern(PathBuilder.root().getPath(),
                PathMatcherBuilder.root().build(),
                true);
    }

    @Test
    public void testIncorrectPath1() {
        assertPattern("..",
                PathMatcherBuilder.root().build(),
                false);
    }

    @Test
    public void testIncorrectPath2() {
        assertPattern(PathBuilder.root().addProperty("abc").getPath(),
                PathMatcherBuilder.root().build(),
                false);
    }

    @Test
    public void testMatchingSpecificArrayIndex() {
        assertPattern(PathBuilder.root().addArrayIndex(1).getPath(),
                PathMatcherBuilder.root().addArrayIndex(1).build(),
                true);
    }

    @Test
    public void testNotMatchingDifferentIndex() {
        assertPattern(PathBuilder.root().addArrayIndex(1).getPath(),
                PathMatcherBuilder.root().addArrayIndex(2).build(),
                false);
    }

    @Test
    public void testNotMatchingAssociativeArrayKeyByArrayIndex() {
        assertPattern(PathBuilder.root().addAssociativeArrayKey("1").getPath(),
                PathMatcherBuilder.root().addArrayIndex(1).build(),
                false);
    }

    @Test
    public void testMatchingAssociativeArrayKey() {
        assertPattern(PathBuilder.root().addAssociativeArrayKey("1").getPath(),
                PathMatcherBuilder.root().addAssociativeArrayKey("1").build(),
                true);
    }

    @Test
    public void testNotMatchingArrayIndexByAssociativeArrayKey() {
        assertPattern(PathBuilder.root().addArrayIndex(1).getPath(),
                PathMatcherBuilder.root().addAssociativeArrayKey("1").build(),
                false);
    }

    @Test
    public void testMatchingProperty() {
        assertPattern(PathBuilder.root().addProperty("abc").getPath(),
                PathMatcherBuilder.root().addProperty("abc").build(),
                true);
    }

    @Test
    public void testNotMatchingProperty() {
        assertPattern(PathBuilder.root().addProperty("abcde").getPath(),
                PathMatcherBuilder.root().addProperty("abc").build(),
                false);
    }

    @Test
    public void testMatchingPathHavingTwoProperties() {
        assertPattern(PathBuilder.root().addProperty("abc").addProperty("def").getPath(),
                PathMatcherBuilder.root().addProperty("abc").addProperty("def").build(),
                true);
    }

    @Test
    public void testMatchingPathHavingPropertyAndArrayIndex() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).getPath(),
                PathMatcherBuilder.root().addProperty("abc").addArrayIndex(1).build(),
                true);
    }

    @Test
    public void testMatchingPathHavingPropertyAndAssociativeArrayKey() {
        assertPattern(PathBuilder.root().addProperty("abc").addAssociativeArrayKey("1").getPath(),
                PathMatcherBuilder.root().addProperty("abc").addAssociativeArrayKey("1").build(),
                true);
    }

    @Test
    public void testMatchingPathHavingPropertyArrayIndexAndProperty() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").build(),
                true);
    }

    @Test
    public void testMatchingAnyPropertyAtPathStart() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addAnyProperty().addArrayIndex(1).addProperty("def").build(),
                true);
    }

    @Test
    public void testMatchingOptionalPropertyWhenOneIsThereToBeMatched() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addOptionalProperty().addArrayIndex(1).addProperty("def").build(),
                true);
    }

    @Test
    public void testMatchingOptionalPropertyWhenThereIsntOneToBeMatched() {
        assertPattern(PathBuilder.root().addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addOptionalProperty().addArrayIndex(1).addProperty("def").build(),
                true);
    }

    @Test
    public void testMatchingAnyPropertyAtPathEnd() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addProperty("abc").addArrayIndex(1).addAnyProperty().build(),
                true);
    }

    @Test
    public void testMatchingAnyArrayIndexAndAnyProperty() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addProperty("abc").addAnyArrayIndex().addAnyProperty().build(),
                true);
    }

    @Test
    public void testMatchingAnyPathComponentFollowedByAnyIndexAndAnyProperty() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addAnyPathComponent().addAnyArrayIndex().addAnyProperty().build(),
                true);
    }

    @Test
    public void testMatchingAnyPathComponentToMatchProperty() {
        assertPattern(PathBuilder.root().addProperty("abc").getPath(),
                PathMatcherBuilder.root().addAnyPathComponent().build(),
                true);
    }

    @Test
    public void testMatchingAnyPathComponentToMatchArrayIndex() {
        assertPattern(PathBuilder.root().addArrayIndex(0).getPath(),
                PathMatcherBuilder.root().addAnyPathComponent().build(),
                true);
    }

    @Test
    public void testMatchingAnyPathComponentToMatchAssociativeArrayKey() {
        assertPattern(PathBuilder.root().addAssociativeArrayKey("0").getPath(),
                PathMatcherBuilder.root().addAnyPathComponent().build(),
                true);
    }

    @Test
    public void testMatchingMultipleUsageOfAnyPathComponent() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root()
                        .addAnyPathComponent()
                        .addAnyPathComponent()
                        .addAnyPathComponent()
                        .build(),
                true);
    }

    @Test
    public void testMatchingPathUsingZeroToMultipleMatcherMatchingAnySubPath1() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addAnySubPath().addAnyPathComponent().addAnyPathComponent().build(),
                true);
    }

    @Test
    public void testMatchingPathUsingZeroToMultipleMatcherMatchingAnySubPath2() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addAnySubPath().addAnyPathComponent().build(),
                true);
    }

    @Test
    public void testMatchingPathUsingZeroToMultipleMatcherMatchingAnySubPath3() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addAnySubPath().build(),
                true);
    }

    @Test
    public void testMatchingPathUsingZeroToMultipleMatcherMatchingAnySubPath4() {
        assertPattern(PathBuilder.root().addProperty("whatever").getPath(),
                PathMatcherBuilder.root().addAnySubPath().addAnyProperty().build(),
                true);
    }

    @Test
    public void testMatchingOptionalAnyPathComponentToMatchProperty() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root().addAnyOptionalPathComponent().addAnyArrayIndex().addAnyProperty().build(),
                true);
    }

    @Test
    public void testMatchingOptionalAnyPathComponentToMatchArrayIndex() {
        assertPattern(PathBuilder.root().addArrayIndex(1).getPath(),
                PathMatcherBuilder.root().addAnyOptionalPathComponent().addAnyArrayIndex().addAnyOptionalPathComponent().build(),
                true);
    }

    @Test
    public void testMatchingOptionalAnyPathComponentToMatchProperty2() {
        assertPattern(PathBuilder.root().addProperty("abc").getPath(),
                PathMatcherBuilder.root().addAnyOptionalPathComponent().build(),
                true);
    }

    @Test
    public void testMatchingOptionalAnyPathComponentToNothing() {
        assertPattern(PathBuilder.root().getPath(),
                PathMatcherBuilder.root().addAnyOptionalPathComponent().build(),
                true);
    }

    @Test
    public void testMatchingOptionalAnyPathComponentToMatchArrayIndex2() {
        assertPattern(PathBuilder.root().addArrayIndex(0).getPath(),
                PathMatcherBuilder.root().addAnyOptionalPathComponent().build(),
                true);
    }

    @Test
    public void testMatchingOptionalAnyPathComponentToMatchAssociativeArrayKey() {
        assertPattern(PathBuilder.root().addAssociativeArrayKey("0").getPath(),
                PathMatcherBuilder.root().addAnyOptionalPathComponent().build(),
                true);
    }

    @Test
    public void testMatchingOptionalAnyPathComponent() {
        assertPattern(PathBuilder.root().addProperty("abc").addArrayIndex(1).addProperty("def").getPath(),
                PathMatcherBuilder.root()
                        .addAnyOptionalPathComponent()
                        .addAnyOptionalPathComponent()
                        .addAnyOptionalPathComponent()
                        .build(),
                true);
    }

    @Test
    public void testMatchingOptionalAnyPathComponentToNothing3() {
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