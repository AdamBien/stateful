package com.airhacks.stateful.definition;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.Status;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.EnterableState;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.commons.scxml2.model.SCXML;
import org.apache.commons.scxml2.model.State;
import org.apache.commons.scxml2.model.TransitionTarget;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class DefinitionTest {

    @Test
    public void readModel() throws XMLStreamException, IOException, ModelException {
        SCXML scxml = SCXMLReader.read(this.getClass().getResourceAsStream("/state.xml"));
        System.out.println(scxml.getTargets());
        SCXMLExecutor executor = new SCXMLExecutor();
        executor.setStateMachine(scxml);
        executor.go();
        System.out.println(" " + getStates(executor));

        trigger(executor, "login");
        trigger(executor, "browse");

        trigger(executor, "logout");

        List<EnterableState> states = scxml.getChildren();
        for (EnterableState state : states) {
            System.out.println("state = " + state.getId());
            if (state instanceof State) {
                State s = (State) state;
                s.getTransitionsList().forEach(p -> System.out.println(p.getEvent() + "->"
                        + p.getTargets().stream().map(t -> t.getId()).collect(Collectors.joining(","))));
            }
        }
        Map<String, TransitionTarget> targets = scxml.getTargets();
        targets.entrySet().forEach(new Consumer<Map.Entry<String, TransitionTarget>>() {

            @Override
            public void accept(Map.Entry<String, TransitionTarget> t) {
                System.out.println(t.getKey() + "->" + t.getValue().getParent());
            }
        });
    }

    public void trigger(SCXMLExecutor executor, String event) throws ModelException {
        executor.triggerEvent(new TriggerEvent(event, TriggerEvent.SIGNAL_EVENT));
        System.out.println("---" + event + "---");
        System.out.println(" " + getStates(executor));
        System.out.println(" " + getActiveStates(executor));
        System.out.println("------------------------");

    }

    public List<String> getStates(SCXMLExecutor executor) {
        Status status = executor.getStatus();
        Set<EnterableState> states = status.getStates();
        return states.stream().map(s -> s.getId()).collect(Collectors.toList());
    }

    public List<String> getActiveStates(SCXMLExecutor executor) {
        Status status = executor.getStatus();
        Set<EnterableState> states = status.getActiveStates();
        return states.stream().map(s -> s.getId()).collect(Collectors.toList());
    }

}
