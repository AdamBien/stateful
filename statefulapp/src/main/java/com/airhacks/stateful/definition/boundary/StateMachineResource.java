package com.airhacks.stateful.definition.boundary;

import java.io.InputStream;
import java.net.URI;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author airhacks.com
 */
public class StateMachineResource {

    DefinitionsManager dm;

    public StateMachineResource(DefinitionsManager dm) {
        this.dm = dm;
    }

    @PUT
    public Response define(@PathParam("stateMachineId") String stateMachineId, InputStream stream, @Context UriInfo info) {
        boolean created = !this.dm.exists(stateMachineId);
        this.dm.create(stateMachineId, stream);
        if (created) {
            URI uri = info.getAbsolutePathBuilder().build();
            return Response.created(uri).build();
        } else {
            return Response.ok().build();
        }
    }

    @GET
    public Response get(@PathParam("stateMachineId") String stateMachineId) {
        if (!this.dm.exists(stateMachineId)) {
            return Response.noContent().build();
        }
        StreamingOutput so = (output) -> {
            dm.dump(stateMachineId, output);
        };

        return Response.ok(so).build();
    }

    @DELETE
    public void remove(@PathParam("stateMachineId") String stateMachineId) {
        dm.remove(stateMachineId);
    }

}
