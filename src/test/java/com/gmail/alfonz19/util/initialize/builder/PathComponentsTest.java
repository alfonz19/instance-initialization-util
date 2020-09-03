package com.gmail.alfonz19.util.initialize.builder;

import com.gmail.alfonz19.util.initialize.context.PathComponent;
import com.gmail.alfonz19.util.initialize.context.PathComponentType;
import com.gmail.alfonz19.util.initialize.context.PathComponents;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class PathComponentsTest {

    @Test
    public void testDotPath() {
        PathComponents pathComponents = new PathComponents(".");
        assertThat(pathComponents.getLastPathComponent(), nullValue());
        assertThat(pathComponents.getComponents(), Matchers.emptyIterable());
    }

    @Test
    public void testEmptyPath() {
        PathComponents pathComponents = new PathComponents("");
        assertThat(pathComponents.getLastPathComponent(), nullValue());
        assertThat(pathComponents.getComponents(), Matchers.emptyIterable());
    }

    @Test
    public void testNullPath() {
        PathComponents pathComponents = new PathComponents(null);
        assertThat(pathComponents.getLastPathComponent(), nullValue());
        assertThat(pathComponents.getComponents(), Matchers.emptyIterable());
    }

    @Test
    public void testPropertyPath() {
        PathComponents pathComponents = new PathComponents(".a");
        assertProperty(pathComponents.getLastPathComponent(), "a");

        assertThat(pathComponents.getComponents().size(), is(1));
        assertProperty(pathComponents.getComponents().get(0), "a");
    }

    @Test
    public void testPropertyPath2() {
        PathComponents pathComponents = new PathComponents(".ab");

        assertProperty(pathComponents.getLastPathComponent(), "ab");

        assertThat(pathComponents.getComponents().size(), is(1));
        assertProperty(pathComponents.getComponents().get(0), "ab");
    }

    @Test
    public void testArrayPath() {
        PathComponents pathComponents = new PathComponents(".ab[0]");

        assertArrayIndex(pathComponents.getLastPathComponent(), 0);

        assertThat(pathComponents.getComponents().size(), is(2));
        assertProperty(pathComponents.getComponents().get(0), "ab");
        assertArrayIndex(pathComponents.getComponents().get(1), 0);
    }

    @Test
    public void testDoubleArrayPath() {
        PathComponents pathComponents = new PathComponents(".ab[0][1]");

        assertArrayIndex(pathComponents.getLastPathComponent(), 1);

        assertThat(pathComponents.getComponents().size(), is(3));
        assertProperty(pathComponents.getComponents().get(0), "ab");
        assertArrayIndex(pathComponents.getComponents().get(1), 0);
        assertArrayIndex(pathComponents.getComponents().get(2), 1);
    }

    @Test
    public void testArrayFollowedByPropertyPath() {
        PathComponents pathComponents = new PathComponents(".ab[1].c");

        assertProperty(pathComponents.getLastPathComponent(), "c");

        assertThat(pathComponents.getComponents().size(), is(3));
        assertProperty(pathComponents.getComponents().get(0), "ab");
        assertArrayIndex(pathComponents.getComponents().get(1), 1);
        assertProperty(pathComponents.getComponents().get(2), "c");
    }

    @Test
    public void testDoubleArrayFollowedByPropertyPath() {
        PathComponents pathComponents = new PathComponents(".ab[1][2].c");
        assertProperty(pathComponents.getLastPathComponent(), "c");

        assertThat(pathComponents.getComponents().size(), is(4));
        assertProperty(pathComponents.getComponents().get(0), "ab");
        assertArrayIndex(pathComponents.getComponents().get(1), 1);
        assertArrayIndex(pathComponents.getComponents().get(2), 2);
        assertProperty(pathComponents.getComponents().get(3), "c");
    }

    @Test(expected = InitializeException.class)
    public void testIncorrectPath() {
        new PathComponents("..");
    }

    @Test(expected = InitializeException.class)
    public void testIncorrectPath2() {
        new PathComponents(".[");
    }

    @Test(expected = InitializeException.class)
    public void testIncorrectPath3() {
        new PathComponents("[");
    }

    @Test(expected = InitializeException.class)
    public void testIncorrectPath4() {
        new PathComponents("]");
    }

    @Test(expected = InitializeException.class)
    public void testIncorrectPath5() {
        new PathComponents(".]");
    }

    @Test(expected = InitializeException.class)
    public void testIncorrectPath6() {
        new PathComponents(".[[]");
    }

    @Test(expected = InitializeException.class)
    public void testIncorrectPath7() {
        new PathComponents(".a.");
    }

    @Test(expected = InitializeException.class)
    public void testIncorrectPath8() {
        new PathComponents(".a[");
    }

    @Test(expected = InitializeException.class)
    public void testIncorrectPath9() {
        new PathComponents(".a]");
    }

    public void assertProperty(PathComponent pathComponent, String expectedValue) {
        assertThat(pathComponent.getValue(), notNullValue());
        assertThat(pathComponent.getValue(), is(expectedValue));
        assertThat(pathComponent.getPathComponentType(), is(PathComponentType.PROPERTY));
    }

    public void assertArrayIndex(PathComponent component, int expectedIndex) {
        assertThat(component, notNullValue());
        assertThat(component.getValue(), is(expectedIndex));
        assertThat(component.getPathComponentType(), is(PathComponentType.ARRAY_INDEX));
    }
}