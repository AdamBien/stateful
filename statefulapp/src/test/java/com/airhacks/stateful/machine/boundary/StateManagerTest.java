package com.airhacks.stateful.machine.boundary;

import com.airhacks.stateful.TestSCXMLExecutorProvider;
import com.airhacks.stateful.machine.control.DefinitionStore;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author airhacks.com
 */
public class StateManagerTest {

    private StateManager cut;
    private DefinitionStore ds;

    @Before
    public void init() {
        this.ds = mock(DefinitionStore.class);
        this.cut = new StateManager();
        this.cut.store = this.ds;
    }

    @Test
    public void trigger() throws Exception {
        String stateMachineId = "test";
        SCXMLExecutor executor = TestSCXMLExecutorProvider.create();
        when(this.ds.find(stateMachineId)).thenReturn(executor);
        String event = "login";
        JsonObject result = this.cut.trigger(stateMachineId, event);
        System.out.println("result = " + result);
        JsonArray results = result.getJsonArray("current-state");
    }

}
