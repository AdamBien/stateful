package com.airhacks.stateful.machine.boundary;

import java.net.URI;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author airhacks.com
 */
public class AttachmentsResource {

    AttachmentManager attachmentManager;

    public AttachmentsResource(AttachmentManager am) {
        this.attachmentManager = am;
    }

    /**
     * Attaches a a JsonObject to the statemachine
     *
     * @param stateMachineId name of the state machine
     * @param attachment a JsonObject containing any arbitrary payload to be
     * attached to the stm.
     * @return 201 with Location header
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transition(@PathParam("stateMachineId") String stateMachineId, @Context UriInfo info, JsonObject attachment) {
        boolean successfullyAttached = attachmentManager.attach(stateMachineId, attachment);
        if (!successfullyAttached) {
            return Response.status(Response.Status.BAD_REQUEST).
                    header("info", "State machine with " + stateMachineId + " not found").
                    build();
        }
        URI uri = info.getAbsolutePathBuilder().build();
        return Response.created(uri).build();
    }

    /**
     * Returns the payload associated with the STM.
     *
     * @param stateMachineId name of the state machine
     * @return the payload as JSON
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response status(@PathParam("stateMachineId") String stateMachineId) {
        JsonObject attachment = attachmentManager.attachment(stateMachineId);
        return Response.ok(attachment).build();
    }

    /**
     * Removes the attachment
     *
     * @param stateMachineId name of the state machine
     * @return Http Status code 204
     */
    @DELETE
    public void reset(@PathParam("stateMachineId") String stateMachineId) {
        attachmentManager.removeAttachment(stateMachineId);
    }

}
