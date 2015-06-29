package com.airhacks.stateful.machine.boundary;

import com.airhacks.stateful.machine.control.DefinitionStore;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.io.SCXMLWriter;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.commons.scxml2.model.SCXML;

/**
 *
 * @author airhacks.com
 */
@Stateless
public class StateMachinesManager {

    @Inject
    DefinitionStore ds;

    public String create(String stateMachineId, InputStream stream) {
        SCXMLExecutor executor = createAndStart(stateMachineId, stream);
        this.ds.store(stateMachineId, executor);
        return stateMachineId;
    }

    SCXMLExecutor createAndStart(String stateMachineId, InputStream stream) {
        SCXML scxml = null;
        try {
            scxml = SCXMLReader.read(stream);
        } catch (IOException | ModelException | XMLStreamException e) {
            throw new RuntimeException("Cannot read stream for state machine id: " + stateMachineId, e);
        }
        SCXMLExecutor executor = new SCXMLExecutor();
        try {
            executor.setStateMachine(scxml);
        } catch (ModelException e) {
            throw new RuntimeException("Invalid model for state machine id: " + stateMachineId, e);
        }
        try {
            executor.go();
        } catch (ModelException e) {
            throw new RuntimeException("Cannot start executor with state machine id: " + stateMachineId, e);
        }
        return executor;
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

    public List<String> stateMachineNames() {
        return this.ds.stateMachineNames();
    }

    public void dump(String stateMachineId, OutputStream output) {
        SCXMLExecutor executor = this.ds.find(stateMachineId);
        if (executor == null) {
            return;
        }
        try {
            SCXMLWriter.Configuration config = new SCXMLWriter.Configuration(null, null, null,
                    StandardCharsets.UTF_8.name(), true, false);
            SCXMLWriter.write(executor.getStateMachine(), output, config);
        } catch (IOException | XMLStreamException ex) {
            throw new IllegalStateException("Cannot serialize state", ex);
        }
    }

    public void remove(String stateMachineId) {
        this.ds.remove(stateMachineId);
    }

}
