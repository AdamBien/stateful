package com.airhacks.stateful;

import com.airhacks.stateful.machine.control.SCXMLExecutorFactory;
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
        SCXMLExecutor executor = SCXMLExecutorFactory.create(scxml);
        executor.go();
        return executor;

    }

}
