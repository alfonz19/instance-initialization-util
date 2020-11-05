package com.gmail.alfonz19.util.initialize.rules;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Rules {
    public static final Rules NO_RULES = new Rules();

    private final List<Rule> ruleList = new LinkedList<>();

    public Rules() {
    }

    public Rules(List<Rule> rules) {
        addRules(rules);
    }

    public Rules addRule(RuleBuilder ruleBuilder) {
        return addRule(ruleBuilder.build());
    }

    public Rules addRule(Rule rule) {
        ruleList.add(rule);
        return this;
    }

    public Rules addRules(List<Rule> rules) {
        ruleList.addAll(Objects.requireNonNull(rules));
        return this;
    }

    public List<Rule> getRuleList() {
        return Collections.unmodifiableList(ruleList);
    }

    public Iterator<Rule> iterator() {
        return this.ruleList.iterator();
    }
}
