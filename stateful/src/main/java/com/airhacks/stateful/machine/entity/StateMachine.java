package com.airhacks.stateful.machine.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import org.apache.commons.scxml2.SCInstance;

/**
 *
 * @author airhacks.com
 */
@Entity
@NamedQuery(name = StateMachine.findAllNames, query = "SELECT s.stateMachineId FROM StateMachine s")
public class StateMachine {

    @Id
    private String stateMachineId;

    private static final String PREFIX = "com.airhacks.stateful.machine.entity.StateMachine.";
    public final static String findAllNames = ".findAllNames";

    @Lob
    private SCInstance state;

    protected StateMachine() {
    }

    public StateMachine(String stateMachineId, SCInstance state) {
        this.stateMachineId = stateMachineId;
        this.state = state;
    }

    public String getStateMachineId() {
        return stateMachineId;
    }

    public SCInstance getState() {
        return state;
    }

}
