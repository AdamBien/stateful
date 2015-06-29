package com.airhacks.stateful.machine.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import org.apache.commons.scxml2.SCInstance;
import org.apache.commons.scxml2.SCXMLExecutor;

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

    public StateMachine(String stateMachineId, SCXMLExecutor executor) {
        this.stateMachineId = stateMachineId;
        this.state = executor.detachInstance();
    }

    protected StateMachine() {
    }

    public String getStateMachineId() {
        return stateMachineId;
    }

    public SCXMLExecutor getSCXMLExecutor() {
        SCXMLExecutor scxmlExecutor = new SCXMLExecutor();
        scxmlExecutor.attachInstance(state);
        return scxmlExecutor;
    }

}
