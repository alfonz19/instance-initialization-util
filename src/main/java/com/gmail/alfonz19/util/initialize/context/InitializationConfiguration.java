package com.gmail.alfonz19.util.initialize.context;

import com.gmail.alfonz19.util.initialize.context.path.InstancePath;
import com.gmail.alfonz19.util.initialize.context.path.Path;
import com.gmail.alfonz19.util.initialize.rules.PredefinedRules;
import com.gmail.alfonz19.util.initialize.rules.Rules;

import java.util.HashMap;
import java.util.Map;

/**
 * Acts as a global configuration for given initialization.
 */
public class InitializationConfiguration {
    private final Map<Path, Rules> rulesForPath = new HashMap<>();
    private final Map<Path, Rules> randomValueGeneratorRulesPerPath = new HashMap<>();

    public InitializationConfiguration() {
        setGlobalRandomValueGeneratorRules(PredefinedRules.basicJavaTypes());
    }

    public InitializationConfiguration(Rules globalRules) {
        this();
        setGlobalRules(globalRules);
    }

    public InitializationConfiguration setGlobalRules(Rules globalRules) {
        rulesForPath.put(InstancePath.ROOT_PATH, globalRules);
        return this;
    }

    public InitializationConfiguration setGlobalRandomValueGeneratorRules(Rules globalRules) {
        randomValueGeneratorRulesPerPath.put(InstancePath.ROOT_PATH, globalRules);
        return this;
    }

    public InitializationConfiguration setRulesForPath(Path path, Rules rules) {
        rulesForPath.put(path, rules);
        return this;
    }


    public InitializationConfiguration setRandomValueGeneratorRulesForPath(Path path, Rules rules) {
        randomValueGeneratorRulesPerPath.put(path, rules);
        return this;
    }

    public Rules getRulesForPath(Path path) {
        return rulesForPath.get(path);
    }

    public Rules getRandomValueGeneratorRulesForPath(Path path) {
        return randomValueGeneratorRulesPerPath.get(path);
    }
}
