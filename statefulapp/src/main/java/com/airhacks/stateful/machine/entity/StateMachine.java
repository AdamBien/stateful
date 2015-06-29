package com.airhacks.stateful.machine.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import org.apache.commons.scxml2.SCInstance;
import org.apache.commons.scxml2.SCXMLExecutor;

/**
 *
 * @author airhacks.com
 */
@Entity
public class StateMachine {

    @Id
    private String stateMachineId;

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
