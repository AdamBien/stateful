package com.airhacks.stateful.machine.boundary;

import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class StatesResourceIT {

    @Rule
    public JAXRSClientProvider machinesBuilder = JAXRSClientProvider.buildWithURI("http://localhost:8080/statefulapp/resources/machines/");

    @Rule
    public JAXRSClientProvider statesBuilder = JAXRSClientProvider.buildWithURI("http://localhost:8080/statefulapp/resources/machines/{stateMachineId}/states/");

    private static final String INITIAL_EVENT_NAME = "login";

    @Test
    public void traverseTransitions() {
        String machineName = initMachine();
        String nextEvent = triggerEvent(machineName, INITIAL_EVENT_NAME);
        nextEvent = triggerEvent(machineName, nextEvent);
        nextEvent = triggerEvent(machineName, nextEvent);
        nextEvent = triggerEvent(machineName, nextEvent);
        System.out.println("Final event = " + nextEvent);
    }

    String initMachine() {
        String key = "duke_" + System.nanoTime();
        WebTarget tut = machinesBuilder.target();
        InputStream stream = this.getClass().getResourceAsStream("/state.xml");
        Response response = tut.path(key).request().put(Entity.entity(stream, MediaType.WILDCARD_TYPE));
        assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));
        return key;
    }

    @Test
    public void status() {
        final String expected = "authenticated";
        String machineName = initMachine();
        triggerEvent(machineName, INITIAL_EVENT_NAME);

        JsonObject status = statesBuilder.target().
                resolveTemplate("stateMachineId", machineName).
                request(MediaType.APPLICATION_JSON).get(JsonObject.class);
        assertNotNull(status);

        String actual = status.getJsonArray("current-state").getString(0);
        assertThat(actual, is(expected));
    }

    String triggerEvent(String key, String event) {
        Response response = statesBuilder.target().resolveTemplate("stateMachineId", key).
                request(MediaType.APPLICATION_JSON).
                put(Entity.json(event(event)));
        assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));
        JsonObject result = response.readEntity(JsonObject.class);
        System.out.println("result = " + result);
        JsonArray nextTransitions = result.getJsonArray("next-transitions");
        JsonObject transition = nextTransitions.getJsonObject(0);
        return transition.keySet().iterator().next();
    }

    public JsonObject event(String event) {
        return Json.createObjectBuilder().
                add("event", event).
                build();
    }

}
