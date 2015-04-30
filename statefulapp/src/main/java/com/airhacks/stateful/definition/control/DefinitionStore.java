package com.airhacks.stateful.definition.control;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.commons.scxml2.model.SCXML;

/**
 *
 * @author airhacks.com
 */
@Singleton
public class DefinitionStore {

    private ConcurrentMap<String, SCXMLExecutor> stateMachines;

    @PostConstruct
    public void init() {
        this.stateMachines = new ConcurrentHashMap<>();
    }

    public SCXML store(String stateMachineId, InputStream stream) {
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
        this.stateMachines.put(stateMachineId, executor);
        return scxml;
    }

    public SCXMLExecutor get(String stateMachineId) {
        return this.stateMachines.get(stateMachineId);
    }

    public void remove(String stateMachineId) {
        this.stateMachines.remove(stateMachineId);
    }

    public boolean exists(String stateMachineId) {
        return this.stateMachines.containsKey(stateMachineId);
    }

    public Set<String> stateMachineNames() {
        return this.stateMachines.keySet();
    }

    public SCXMLExecutor find(String stateMachineId) {
        return this.stateMachines.get(stateMachineId);
    }

}
