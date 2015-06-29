package com.airhacks.stateful;

import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.SCXML;

/**
 *
 * @author airhacks.com
 */
public class TestSCXMLExecutorProvider {

    public static SCXMLExecutor create() throws Exception {
        SCXML scxml = SCXMLReader.read(TestSCXMLExecutorProvider.class.getResourceAsStream("/state.xml"));
        SCXMLExecutor executor = new SCXMLExecutor();
        executor.setStateMachine(scxml);
        executor.go();
        return executor;

    }

}
