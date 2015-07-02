package com.airhacks.stateful.machine.boundary;

import com.airhacks.stateful.machine.control.StateMachineStore;
import com.airhacks.stateful.machine.entity.StateMachine;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

/**
 *
 * @author airhacks.com
 */
public class AttachmentManager {

    @Inject
    StateMachineStore sms;

    public void attach(String stateMachineId, JsonObject attachment) {
        StateMachine machine = sms.findStateMachine(stateMachineId);
        try {
            byte[] serialized = serialize(attachment);
            machine.setAttachment(serialized);
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot serialize attachment", ex);
        }
    }

    public JsonObject attachment(String stateMachineId) {
        StateMachine stateMachine = sms.findStateMachine(stateMachineId);
        if (stateMachine == null) {
            return null;
        }
        byte[] deserialized = stateMachine.getAttachment();
        if (deserialized == null) {
            return null;
        }
        try {
            return deserialize(deserialized);
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot deserialize attachment", ex);
        }

    }

    public void removeAttachment(String stateMachineId) {
        StateMachine found = sms.findStateMachine(stateMachineId);
        if (found != null) {
            found.setAttachment(null);
        }
    }

    JsonObject deserialize(byte[] content) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(content)) {
            JsonReader reader = Json.createReader(bais);
            return reader.readObject();
        }
    }

    byte[] serialize(JsonObject object) throws IOException {
        try (ByteArrayOutputStream oos = new ByteArrayOutputStream()) {
            JsonWriter writer = Json.createWriter(oos);
            writer.writeObject(object);
            return oos.toByteArray();
        }
    }

}
