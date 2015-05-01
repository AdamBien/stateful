package com.airhacks.stateful.states.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Path("states")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatesResource {

    @Inject
    StateManager sm;

    @PUT
    @Path("{stateMachineId}")
    public Response transition(@PathParam("stateMachineId") String stateMachineId, JsonObject event) {
        JsonObject result = sm.trigger(stateMachineId, event.getString("event"));
        return Response.ok(result).build();
    }

}
