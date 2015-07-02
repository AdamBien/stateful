package com.airhacks.stateful.machine.boundary;

import com.airhacks.stateful.TestSCXMLExecutorProvider;
import com.airhacks.stateful.machine.control.StateMachineStore;
import com.airhacks.stateful.machine.control.SCXMLExecutorFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.apache.commons.scxml2.SCInstance;
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
    private StateMachineStore ds;

    @Before
    public void init() {
        this.ds = mock(StateMachineStore.class);
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

    //reproducer
    @Ignore
    @Test
    public void triggerWithParametersWithDetachment() throws Exception {
        String stateMachineId = "test";
        SCXMLExecutor executor = TestSCXMLExecutorProvider.create();
        SCInstance detachInstance = executor.detachInstance();
        SCInstance reserialized = reserialize(detachInstance);
        SCXMLExecutor copy = SCXMLExecutorFactory.create(reserialized);

        when(this.ds.find(stateMachineId)).thenReturn(copy);
        JsonObject condition = Json.createObjectBuilder().add("user", "hacker").build();
        String event = "login";
        JsonObject result = this.cut.trigger(stateMachineId, event, condition);
        //serialization after state change fails
        reserialize(reserialized);
        System.out.println("result = " + result);
        JsonArray results = result.getJsonArray("current-state");
        String hackerState = results.getString(0);
        assertThat(hackerState, is("isolated"));
    }

    SCInstance reserialize(SCInstance instance) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(stream);
        oos.writeObject(instance);
        oos.flush();
        byte[] bytes = stream.toByteArray();
        ByteArrayInputStream iis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(iis);
        return (SCInstance) ois.readObject();
    }
}
