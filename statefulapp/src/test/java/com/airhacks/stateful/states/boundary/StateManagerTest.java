package com.airhacks.stateful.states.boundary;

import com.airhacks.stateful.definition.control.DefinitionStore;
import java.io.IOException;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.commons.scxml2.model.SCXML;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class StateManagerTest {

    DefinitionStore ds;
    StateManager cut;

    @Before
    public void init() {
        this.ds = new DefinitionStore();
        this.ds.init();
        this.cut = new StateManager();
        this.cut.store = this.ds;
    }

    @Test
    public void trigger() throws IOException, ModelException, XMLStreamException {
        String stateMachineId = "test";
        SCXML scxml = SCXMLReader.read(this.getClass().getResourceAsStream("/state.xml"));
        SCXMLExecutor executor = new SCXMLExecutor();
        executor.setStateMachine(scxml);
        executor.go();
        this.ds.store(stateMachineId, executor);
        String event = "login";
        JsonObject result = this.cut.trigger(stateMachineId, event);
        System.out.println("result = " + result);
        JsonArray results = result.getJsonArray("current-state");
    }

}
