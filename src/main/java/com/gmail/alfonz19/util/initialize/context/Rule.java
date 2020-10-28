package com.gmail.alfonz19.util.initialize.context;

import com.gmail.alfonz19.util.initialize.generator.Generator;

public interface Rule {
    boolean applies(PathNode pathNode);
    Generator<?> getGenerator();
}
