package com.airhacks.stateful.machine.control;

import com.airhacks.stateful.machine.entity.StateMachine;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;
import org.apache.commons.scxml2.SCXMLExecutor;

/**
 *
 * @author airhacks.com
 */
@Singleton
@Alternative
public class InMemoryStateMachineStore implements StateMachineStore {

    private ConcurrentMap<String, StateMachineHolder> stateMachines;

    @PostConstruct
    public void init() {
        this.stateMachines = new ConcurrentHashMap<>();
    }

    public void store(String stateMachineId, SCXMLExecutor executor) {
        this.stateMachines.put(stateMachineId, new StateMachineHolder(stateMachineId, executor));
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
        StateMachineHolder holder = this.stateMachines.get(stateMachineId);
        if (holder == null) {
            return null;
        }
        return holder.getExecutor();
    }

    @Override
    public StateMachine findStateMachine(String stateMachineId) {
        StateMachineHolder holder = this.stateMachines.get(stateMachineId);
        if (holder == null) {
            return null;
        }
        return holder.getMachine();
    }

}
