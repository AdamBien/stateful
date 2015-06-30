package com.airhacks.stateful.machine.boundary;

import com.airhacks.stateful.TestSCXMLExecutorProvider;
import com.airhacks.stateful.machine.control.DefinitionStore;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.apache.commons.scxml2.SCXMLExecutor;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Ignore;
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
        JsonObject result = this.cut.trigger(stateMachineId, event, null);
        System.out.println("result = " + result);
        JsonArray results = result.getJsonArray("current-state");
    }

    @Ignore
    @Test
    public void triggerWithParameters() throws Exception {
        String stateMachineId = "test";
        SCXMLExecutor executor = TestSCXMLExecutorProvider.create();
        when(this.ds.find(stateMachineId)).thenReturn(executor);
        JsonObject condition = Json.createObjectBuilder().add("user", "hacker").build();
        String event = "login";
        JsonObject result = this.cut.trigger(stateMachineId, event, condition);
        System.out.println("result = " + result);
        JsonArray results = result.getJsonArray("current-state");
        String hackerState = results.getString(0);
        assertThat(hackerState, is("isolated"));
    }

}
