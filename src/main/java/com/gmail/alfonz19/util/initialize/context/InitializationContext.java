package com.gmail.alfonz19.util.initialize.context;

import com.gmail.alfonz19.util.initialize.context.path.InstancePath;
import com.gmail.alfonz19.util.initialize.context.path.Path;
import com.gmail.alfonz19.util.initialize.rules.Rules;

import java.util.HashMap;
import java.util.Map;

/**
 * Acts as a global configuration for given initialization.
 */
public class InitializationContext {
    private final Map<Path, Rules> rulesForPath = new HashMap<>();

    public InitializationContext() {
    }

    public InitializationContext(Rules globalRules) {
        rulesForPath.put(InstancePath.ROOT_PATH, globalRules);
    }

    public Rules getRulesForPath(Path path) {
        return rulesForPath.get(path);
    }
}
