package com.gmail.alfonz19.util.initialize.builder;

import com.gmail.alfonz19.util.initialize.context.InitializationContext;
import com.gmail.alfonz19.util.initialize.context.path.InstancePath;
import com.gmail.alfonz19.util.initialize.context.path.Path;
import com.gmail.alfonz19.util.initialize.context.path.builder.PathBuilder;
import com.gmail.alfonz19.util.initialize.rules.Rules;

public class ConfigurationBuilder {
    private final InitializationContext initializationContext = new InitializationContext();

    public ConfigurationBuilder setGlobalRules(Rules globalRules) {
        initializationContext.setGlobalRules(globalRules);
        return this;
    }

    public ConfigurationBuilder setGlobalRandomValueGeneratorRules(Rules globalRules) {
        initializationContext.setGlobalRandomValueGeneratorRules(globalRules);
        return this;
    }

    public ConfigurationBuilder setRulesForPath(PathBuilder pathBuilder, Rules rules) {
        //TODO MMUCHA: PathBuilder not building path, that's not cool. After method/fields support is in place, I should revisit Path hierarchy and get rid of it eventually, then simplify PathBuilder and this.
        return setRulesForPath(new InstancePath(pathBuilder.getPath()), rules);
    }

    public ConfigurationBuilder setRulesForPath(Path path, Rules rules) {
        initializationContext.setRulesForPath(path, rules);
        return this;
    }

    public ConfigurationBuilder setRandomValueGeneratorRulesForPath(Path path, Rules rules) {
        initializationContext.setRandomValueGeneratorRulesForPath(path, rules);
        return this;
    }

    public InitializationContext getInitializationContext() {
        return initializationContext;
    }
}
