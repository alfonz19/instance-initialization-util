package com.gmail.alfonz19.util.initialize.generator;

import com.gmail.alfonz19.util.initialize.context.Rule;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Rules {
    public static final Rules NO_RULES = new Rules();

    private final List<Rule> ruleList = new LinkedList<>();

    public Rules enableJpaRules() {
        return this;
    }

    public Rules addRule(RuleBuilder ruleBuilder) {
        return addRule(ruleBuilder.build());
    }

    public Rules addRule(Rule build) {
        ruleList.add(build);
        return this;
    }

    public Iterator<Rule> iterator() {
        return ruleList.iterator();
    }
}
