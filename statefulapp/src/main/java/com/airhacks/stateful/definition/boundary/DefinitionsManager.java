package com.airhacks.stateful.definition.boundary;

import com.airhacks.stateful.definition.control.DefinitionStore;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.scxml2.io.SCXMLWriter;
import org.apache.commons.scxml2.model.SCXML;

/**
 *
 * @author airhacks.com
 */
@Stateless
public class DefinitionsManager {

    @Inject
    DefinitionStore ds;

    public String create(String stateMachineId, InputStream stream) {
        this.ds.createStateMachine(stateMachineId, stream);
        return stateMachineId;
    }

    public boolean exists(String stateMachineId) {
        return ds.exists(stateMachineId);
    }

    public String create(InputStream stream) {
        UUID uuid = UUID.randomUUID();
        String stateMachineId = uuid.toString();
        this.create(stateMachineId, stream);
        return stateMachineId;
    }

    public Set<String> stateMachineNames() {
        return this.ds.stateMachineNames();
    }

    public void dump(String stateMachineId, OutputStream output) {
        SCXML scxml = this.ds.find(stateMachineId);
        try {
            SCXMLWriter.write(scxml, output);
        } catch (IOException | XMLStreamException ex) {
            throw new IllegalStateException("Cannot serialize state", ex);
        }
    }

}
