package com.gmail.alfonz19.util.initialize.context;

public interface PathMatcher {

    boolean matches(String input);

    boolean matches(Path path);

    String describe();
}
