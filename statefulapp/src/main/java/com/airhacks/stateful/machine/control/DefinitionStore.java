package com.airhacks.stateful.machine.control;

import com.airhacks.stateful.machine.entity.StateMachine;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.scxml2.SCXMLExecutor;

/**
 *
 * @author airhacks.com
 */
@Stateless
public class DefinitionStore {

    @PersistenceContext
    EntityManager em;

    public void store(String stateMachineId, SCXMLExecutor executor) {
        this.em.merge(new StateMachine(stateMachineId, executor));
    }

    public SCXMLExecutor find(String stateMachineId) {
        StateMachine machine = this.em.find(StateMachine.class, stateMachineId);
        if (machine == null) {
            return null;
        }
        return machine.getSCXMLExecutor();
    }

    public void remove(String stateMachineId) {
        StateMachine machine = this.em.find(StateMachine.class, stateMachineId);
        if (machine != null) {
            this.em.remove(machine);
        }
    }

    public boolean exists(String stateMachineId) {
        return this.find(stateMachineId) != null;
    }

    public List<String> stateMachineNames() {
        return this.em.createNamedQuery(StateMachine.findAllNames, String.class).
                getResultList();
    }

}
