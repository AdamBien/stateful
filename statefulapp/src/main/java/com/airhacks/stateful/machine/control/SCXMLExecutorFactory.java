package com.airhacks.stateful.machine.control;

import com.airhacks.stateful.machine.entity.SerializableErrorReporter;
import org.apache.commons.scxml2.SCInstance;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.commons.scxml2.model.SCXML;

/**
 *
 * @author airhacks.com
 */
public class SCXMLExecutorFactory {

    public static SCXMLExecutor create(SCXML scxml) throws ModelException {
        SCXMLExecutor executor = new SCXMLExecutor(new CustomJSEvaluator(), null, null);
        executor.setStateMachine(scxml);
        executor.setEvaluator(new CustomJSEvaluator());
        executor.setErrorReporter(new SerializableErrorReporter());
        return executor;
    }

    public static SCXMLExecutor create(SCInstance state) throws ModelException {
        SCXMLExecutor executor = new SCXMLExecutor(new CustomJSEvaluator(), null, null);
        executor.attachInstance(state);
        executor.setErrorReporter(new SerializableErrorReporter());
        return executor;
    }
}
