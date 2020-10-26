package com.gmail.alfonz19.testsupport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gmail.alfonz19.util.initialize.TestInitAnywhereInTreeStringPropertyWhichNameContainsGivenTextTest;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.junit.AssumptionViolatedException;
import org.junit.runner.Description;

@Slf4j(topic = "TESTS")
public class InitializedInstanceLogger extends org.junit.rules.TestWatcher {

    private static final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
    private Description description;

    protected void starting(Description description) {
        this.description = description;
    }

    @Override
    protected void succeeded(Description description) {
    }

    @Override
    protected void failed(Throwable e, Description description) {
    }

    @Override
    protected void skipped(AssumptionViolatedException e, Description description) {
    }

    @Override
    protected void finished(Description description) {
    }

    public void logInitializedInstance(Object testInstance) {
        try {
            log.debug("Instance initialized in test {}:\n{}", description.getClassName()+"#"+description.getMethodName(), objectWriter.writeValueAsString(testInstance));
        } catch (JsonProcessingException e) {
            throw new InitializeException(e);
        }
    }
}
