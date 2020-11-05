package com.gmail.alfonz19.util.initialize.builder;

import com.gmail.alfonz19.util.initialize.context.InitializationConfiguration;
import com.gmail.alfonz19.util.initialize.context.path.InstancePath;
import com.gmail.alfonz19.util.initialize.context.path.Path;
import com.gmail.alfonz19.util.initialize.context.path.builder.PathBuilder;
import com.gmail.alfonz19.util.initialize.rules.Rules;

public class InitializationConfigurationBuilder {
    private final InitializationConfiguration initializationConfiguration = new InitializationConfiguration();

    public InitializationConfigurationBuilder setGlobalRules(Rules globalRules) {
        initializationConfiguration.setGlobalRules(globalRules);
        return this;
    }

    public InitializationConfigurationBuilder setGlobalRandomValueGeneratorRules(Rules globalRules) {
        initializationConfiguration.setGlobalRandomValueGeneratorRules(globalRules);
        return this;
    }

    public InitializationConfigurationBuilder setRulesForPath(PathBuilder pathBuilder, Rules rules) {
        //TODO MMUCHA: PathBuilder not building path, that's not cool. After method/fields support is in place, I should revisit Path hierarchy and get rid of it eventually, then simplify PathBuilder and this.
        return setRulesForPath(new InstancePath(pathBuilder.getPath()), rules);
    }

    public InitializationConfigurationBuilder setRulesForPath(Path path, Rules rules) {
        initializationConfiguration.setRulesForPath(path, rules);
        return this;
    }

    public InitializationConfigurationBuilder setRandomValueGeneratorRulesForPath(Path path, Rules rules) {
        initializationConfiguration.setRandomValueGeneratorRulesForPath(path, rules);
        return this;
    }

    public InitializationConfiguration getinitializationConfiguration() {
        return initializationConfiguration;
    }
}
