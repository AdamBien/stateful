package com.airhacks.stateful.definition.control;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import org.apache.commons.scxml2.SCXMLExecutor;

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

    public void store(String stateMachineId, SCXMLExecutor executor) {
        this.stateMachines.put(stateMachineId, executor);
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
