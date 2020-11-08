package com.gmail.alfonz19.testsupport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gmail.alfonz19.util.initialize.exception.InitializeException;
import lombok.extern.slf4j.Slf4j;

import org.junit.AssumptionViolatedException;
import org.junit.runner.Description;

@Slf4j(topic = "TEST_DEBUG")
public class InitializedInstanceTestLogging extends org.junit.rules.TestWatcher {

    private static final ObjectWriter objectWriter = createObjectWriter();

    private static ObjectWriter createObjectWriter() {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().without(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    private Description description;

    protected void starting(Description description) {
        this.description = description;
        log.debug("Testing {}: ", description.getTestClass().getSimpleName()+"#"+description.getMethodName());
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
        log.debug("—————————— end of test ——————————————————————————");
    }

    public void logInitializedInstance(Object testInstance) {
        try {
            log.debug("Initialized instance as JSON:\n{}", objectWriter.writeValueAsString(testInstance));
        } catch (JsonProcessingException e) {
            throw new InitializeException(e);
        }
    }
}
