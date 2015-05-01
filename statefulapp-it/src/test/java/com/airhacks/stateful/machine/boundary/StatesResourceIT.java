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

    @Test
    public void traverseTransitions() {

        String key = "duke_" + System.nanoTime();
        WebTarget tut = machinesBuilder.target();

        InputStream stream = this.getClass().getResourceAsStream("/state.xml");
        Response response = tut.path(key).request().put(Entity.entity(stream, MediaType.WILDCARD_TYPE));
        assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));

        String nextEvent = triggerEvent(key, "login");
        nextEvent = triggerEvent(key, nextEvent);
        nextEvent = triggerEvent(key, nextEvent);
        nextEvent = triggerEvent(key, nextEvent);
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
