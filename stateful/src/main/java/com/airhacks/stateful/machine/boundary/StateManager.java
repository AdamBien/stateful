package com.airhacks.stateful.machine.boundary;

import com.airhacks.stateful.machine.control.StateMachineStore;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import org.apache.commons.scxml2.Context;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.Status;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.EnterableState;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.commons.scxml2.model.State;
import org.apache.commons.scxml2.model.Transition;

/**
 *
 * @author airhacks.com
 */
@Stateless
public class StateManager {

    @Inject
    StateMachineStore store;

    public JsonObject trigger(String stateMachineId, String event, JsonObject payload) {
        SCXMLExecutor executor = store.find(stateMachineId);
        if (payload != null) {
            passParameters(payload, executor.getRootContext());
        }
        try {
            executor.triggerEvent(new TriggerEvent(event, TriggerEvent.SIGNAL_EVENT));
            this.store.store(stateMachineId, executor);
        } catch (ModelException ex) {
            throw new IllegalStateException("Cannot trigger event: " + event + " with stm: " + stateMachineId, ex);
        }
        return status(stateMachineId);
    }

    public JsonObject reset(String stateMachineId) {
        SCXMLExecutor executor = store.find(stateMachineId);
        try {
            executor.reset();
        } catch (ModelException ex) {
            throw new IllegalStateException("Cannot reset executor with stm: " + stateMachineId, ex);
        }
        //TODO: woraround -- reset makes the StateMachine not Serializable
        store.store(stateMachineId, executor);
        return status(stateMachineId);
    }

    void passParameters(JsonObject object, Context context) {
        Set<String> keyNames = object.keySet();
        for (String name : keyNames) {
            JsonValue.ValueType type = object.get(name).getValueType();
            switch (type) {
                case STRING:
                    context.set(name, object.getString(name));
                    break;
                case NUMBER:
                    context.set(name, object.getJsonNumber(name).doubleValue());
                    break;
                case NULL:
                    context.set(name, null);
                    break;
                case FALSE:
                    context.set(name, false);
                    break;
                case TRUE:
                    context.set(name, true);
                    break;
                case ARRAY:
                    context.set(name, object.getJsonArray(name));
                    break;
                case OBJECT:
                    context.set(name, object.getJsonObject(name));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown JSON value");
            }
        }

    }

    public JsonObject status(String stateMachineId) {
        SCXMLExecutor executor = store.find(stateMachineId);
        JsonObjectBuilder resultBuilder = Json.createObjectBuilder();
        resultBuilder.add("current-state", getStates(executor));
        Set<EnterableState> states = executor.getStatus().getStates();
        JsonArrayBuilder nextStatesBuilder = Json.createArrayBuilder();
        states.stream().map(s -> nextTargets(s)).forEach(nextStatesBuilder::add);
        resultBuilder.add("next-transitions", nextStatesBuilder.build());
        return resultBuilder.build();
    }

    JsonArray getStates(SCXMLExecutor executor) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        Status status = executor.getStatus();
        Set<EnterableState> states = status.getStates();
        states.stream().map(s -> s.getId()).forEach(arrayBuilder::add);
        return arrayBuilder.build();
    }

    JsonObject nextTargets(EnterableState state) {
        JsonObjectBuilder transitionsBuilder = Json.createObjectBuilder();
        if (state instanceof State) {
            State s = (State) state;
            s.getTransitionsList().forEach(p
                    -> transitionsBuilder.add(p.getEvent(), extractTargets(p))
            );
        }
        return transitionsBuilder.build();
    }

    JsonArray extractTargets(Transition transition) {
        JsonArrayBuilder targetsBuilder = Json.createArrayBuilder();
        transition.getTargets().stream().map(t -> t.getId()).forEach(targetsBuilder::add);
        return targetsBuilder.build();
    }

}
