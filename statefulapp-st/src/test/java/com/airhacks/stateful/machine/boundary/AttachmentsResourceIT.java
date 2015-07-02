package com.airhacks.stateful.machine.boundary;

import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class AttachmentsResourceIT {

    @Rule
    public JAXRSClientProvider provider = JAXRSClientProvider.buildWithURI("http://localhost:8080/statefulapp/resources/machines/{stateMachineId}/attachments/");

    @Rule
    public JAXRSClientProvider machinesBuilder = JAXRSClientProvider.buildWithURI("http://localhost:8080/statefulapp/resources/machines/");

    private String machineName;

    @Before
    public void initMachine() {
        String key = "duke_" + System.nanoTime();
        WebTarget tut = machinesBuilder.target();
        InputStream stream = StatesResourceIT.class.getResourceAsStream("/state.xml");
        Response response = tut.path(key).request().put(Entity.entity(stream, MediaType.WILDCARD_TYPE));
        assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));
        this.machineName = key;
    }

    @Test
    public void attachments() {

        JsonObject attachment = Json.createObjectBuilder().
                add("addition", "info").
                add("answer", 42).
                build();

        //save attachment
        Response response = provider.target().
                resolveTemplate("stateMachineId", machineName).
                request(MediaType.APPLICATION_JSON).
                put(Entity.json(attachment));

        assertThat(response.getStatus(), is(201));
        String locationHeader = response.getHeaderString("Location");
        assertNotNull(locationHeader);

        System.out.println("locationHeader = " + locationHeader);
        //read with location header
        JsonObject savedAttachment = provider.target(locationHeader).
                request(MediaType.APPLICATION_JSON).
                get(JsonObject.class);

        assertNotSame(savedAttachment, attachment);
        assertThat(savedAttachment, is(attachment));

        //remove attachment
        response = provider.target().
                resolveTemplate("stateMachineId", machineName).
                request(MediaType.APPLICATION_JSON).
                delete();
        assertThat(response.getStatus(), is(204));

        //read non-existing
        response = provider.target(locationHeader).
                request(MediaType.APPLICATION_JSON).
                get();
        assertThat(response.getStatus(), is(204));

    }
}
