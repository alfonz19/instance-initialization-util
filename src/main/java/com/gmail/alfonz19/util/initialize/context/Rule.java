package com.gmail.alfonz19.util.initialize.context;

import com.gmail.alfonz19.util.initialize.generator.AbstractGenerator;

public interface Rule {
    //TODO MMUCHA: here we can probably remove class type as this should be reachable from pathContext.
    boolean appliesForPathAndType(PathContext pathContext, Class<?> classType);

    AbstractGenerator<?> getGenerator();
}
