package com.airhacks.stateful.machine.control;

import com.airhacks.stateful.machine.entity.StateMachine;
import java.util.List;
import javax.enterprise.inject.Alternative;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.scxml2.SCInstance;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.model.ModelException;

/**
 *
 * @author airhacks.com
 */
@Alternative
public class JPAStateMachineStore implements StateMachineStore {

    @PersistenceContext
    EntityManager em;

    @Override
    public void store(String stateMachineId, SCXMLExecutor executor) {
        this.em.merge(new StateMachine(stateMachineId, executor.detachInstance()));
    }

    @Override
    public StateMachine findStateMachine(String stateMachineId) {
        return this.em.find(StateMachine.class, stateMachineId);
    }

    @Override
    public SCXMLExecutor find(String stateMachineId) {
        StateMachine machine = this.findStateMachine(stateMachineId);
        if (machine == null) {
            return null;
        }
        SCInstance state = machine.getState();
        try {
            return SCXMLExecutorFactory.create(state);
        } catch (ModelException ex) {
            throw new IllegalStateException("Cannot attach state", ex);
        }
    }

    @Override
    public void remove(String stateMachineId) {
        StateMachine machine = this.em.find(StateMachine.class, stateMachineId);
        if (machine != null) {
            this.em.remove(machine);
        }
    }

    @Override
    public boolean exists(String stateMachineId) {
        return this.find(stateMachineId) != null;
    }

    @Override
    public List<String> stateMachineNames() {
        return this.em.createNamedQuery(StateMachine.findAllNames, String.class).
                getResultList();
    }

}
