package com.airhacks.stateful.definition.boundary;

import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import java.io.InputStream;
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
public class StateMachinesResourceIT {

    @Rule
    public JAXRSClientProvider builder = JAXRSClientProvider.buildWithURI("http://localhost:8080/statefulapp/resources/machines");

    @Test
    public void crud() {
        WebTarget tut = builder.target();
        Response response = tut.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));

        InputStream stream = this.getClass().getResourceAsStream("/state.xml");
        response = tut.request().post(Entity.entity(stream, MediaType.WILDCARD_TYPE));
        assertThat(response.getStatus(), is(201));
        String location = response.getHeaderString("Location");
        assertNotNull(location);

        WebTarget definitionTarget = builder.target(location);
        response = definitionTarget.request().get();
        assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));

        response = builder.target(location).request().delete();
        assertNotNull(response);
        assertThat(response.getStatus(), is(204));

        response = definitionTarget.request().get();
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void put() {
        String key = "duke_" + System.nanoTime();
        WebTarget tut = builder.target();

        InputStream stream = this.getClass().getResourceAsStream("/state.xml");
        Response response = tut.path(key).request().put(Entity.entity(stream, MediaType.WILDCARD_TYPE));
        assertThat(response.getStatus(), is(201));
        String location = response.getHeaderString("Location");
        assertNotNull(location);

        WebTarget definitionTarget = builder.target(location);
        response = definitionTarget.request().get();
        assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));

        //update
        stream = this.getClass().getResourceAsStream("/state.xml");
        response = definitionTarget.request().put(Entity.entity(stream, MediaType.WILDCARD_TYPE));
        assertThat(response.getStatus(), is(200));

    }

}
