package com.airhacks.stateful.machine.control;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Specializes;
import javax.inject.Singleton;
import org.apache.commons.scxml2.SCXMLExecutor;

/**
 *
 * @author airhacks.com
 */
@Alternative
@Singleton
@Specializes
public class InMemoryDefinitionStore extends DefinitionStore {

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

    public List<String> stateMachineNames() {
        return new ArrayList(this.stateMachines.keySet());
    }

    public SCXMLExecutor find(String stateMachineId) {
        return this.stateMachines.get(stateMachineId);
    }

}
