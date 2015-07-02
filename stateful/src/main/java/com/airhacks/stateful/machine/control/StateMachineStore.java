package com.airhacks.stateful.machine.control;

import com.airhacks.stateful.machine.entity.StateMachine;
import java.util.List;
import org.apache.commons.scxml2.SCXMLExecutor;

/**
 *
 * @author airhacks.com
 */
public interface StateMachineStore {

    boolean exists(String stateMachineId);

    SCXMLExecutor find(String stateMachineId);

    StateMachine findStateMachine(String stateMachineId);

    void remove(String stateMachineId);

    List<String> stateMachineNames();

    void store(String stateMachineId, SCXMLExecutor executor);

}
