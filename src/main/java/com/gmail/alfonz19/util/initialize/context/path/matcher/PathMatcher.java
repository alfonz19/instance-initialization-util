package com.gmail.alfonz19.util.initialize.context.path.matcher;

import com.gmail.alfonz19.util.initialize.context.path.Path;

public interface PathMatcher {

    boolean matches(String input);

    boolean matches(Path path);

    String describe();
}
