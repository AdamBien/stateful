package com.airhacks.stateful.machine.control;

import com.airhacks.stateful.machine.entity.StateMachine;
import org.apache.commons.scxml2.SCXMLExecutor;

/**
 *
 * @author airhacks.com
 */
class StateMachineHolder {

    private StateMachine machine;
    private SCXMLExecutor executor;

    public StateMachineHolder(String id, SCXMLExecutor executor) {
        this.machine = new StateMachine(id, null);
        this.executor = executor;
    }

    public StateMachine getMachine() {
        return machine;
    }

    public SCXMLExecutor getExecutor() {
        return executor;
    }

}
