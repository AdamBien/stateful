package com.airhacks.stateful.machine.boundary;

import java.io.InputStream;
import java.net.URI;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author airhacks.com
 */
public class StateMachineResource {

    AttachmentManager am;
    StateMachinesManager dm;
    StateManager sm;

    @Context
    ResourceContext rc;

    public StateMachineResource(StateMachinesManager dm, StateManager sm, AttachmentManager am) {
        this.dm = dm;
        this.sm = sm;
        this.am = am;
    }

    /**
     *
     * @param stateMachineId unique identifier for a SCXML configuration
     * @param stream a valid SCXML file
     * @return 201 for created state machine and 200 for update
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
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

    /**
     *
     * @param stateMachineId name of the state machine
     * @return the SCXML configuration as stream.
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response get(@PathParam("stateMachineId") String stateMachineId) {
        if (!this.dm.exists(stateMachineId)) {
            return Response.noContent().build();
        }
        StreamingOutput so = (output) -> {
            dm.dump(stateMachineId, output);
        };

        return Response.ok(so).build();
    }

    /**
     * Removes and resets the state machine
     *
     * @param stateMachineId the name of the state machine
     */
    @DELETE
    public void remove(@PathParam("stateMachineId") String stateMachineId) {
        dm.remove(stateMachineId);
    }

    @Path("states")
    public StatesResource states() {
        return this.rc.initResource(new StatesResource(this.sm));
    }

    @Path("attachments")
    public AttachmentsResource attachments() {
        return this.rc.initResource(new AttachmentsResource(this.am));
    }

}
