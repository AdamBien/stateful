package com.airhacks.stateful.machine.boundary;

import java.io.InputStream;
import java.net.URI;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Path("machines")
public class StateMachinesResource {

    @Inject
    StateMachinesManager dm;

    @Inject
    StateManager sm;

    @Context
    ResourceContext rc;

    @Path("{stateMachineId}")
    public StateMachineResource stateMachine() {
        return rc.initResource(new StateMachineResource(dm, sm));
    }

    /**
     *
     * @return names of all known state machines
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray all() {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        this.dm.stateMachineNames().forEach(builder::add);
        return builder.build();
    }

    /**
     * This is the only non-idempotent method.
     *
     * @param stream a valid SXCML file
     * @return a Location header containing the UUID of the create machine
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_XML)
    public Response define(InputStream stream, @Context UriInfo info) {
        String id = this.dm.create(stream);
        URI uri = info.getAbsolutePathBuilder().path("/" + id).build();
        return Response.created(uri).build();
    }

}
