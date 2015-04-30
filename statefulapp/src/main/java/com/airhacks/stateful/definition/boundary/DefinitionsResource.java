package com.airhacks.stateful.definition.boundary;

import java.io.InputStream;
import java.net.URI;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Path("definitions")
public class DefinitionsResource {

    @Inject
    DefinitionsManager dm;

    @PUT
    @Path("{stateMachineId}")
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

    @POST
    public Response define(InputStream stream, @Context UriInfo info) {
        String id = this.dm.create(stream);
        URI uri = info.getAbsolutePathBuilder().path("/" + id).build();
        return Response.created(uri).build();
    }

    @GET
    public JsonArray all() {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        this.dm.stateMachineNames().forEach(builder::add);
        return builder.build();
    }

    @GET
    @Path("{stateMachineId}")
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
    @Path("{stateMachineId}")
    public void remove(@PathParam("stateMachineId") String stateMachineId) {
        dm.remove(stateMachineId);
    }

}
