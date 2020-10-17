package com.gmail.alfonz19.util.initialize.context;

import com.gmail.alfonz19.util.initialize.generator.AbstractGenerator;

public interface Rule {
    boolean appliesForPathAndType(PathNode pathNode);
    AbstractGenerator<?> getGenerator();
}
