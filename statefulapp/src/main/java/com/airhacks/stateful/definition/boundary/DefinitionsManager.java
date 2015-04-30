package com.airhacks.stateful.definition.boundary;

import com.airhacks.stateful.definition.control.DefinitionStore;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
@Stateless
public class DefinitionsManager {

    @Inject
    DefinitionStore ds;

    public String create(String stateMachineId, InputStream stream) {
        this.ds.createStateMachine(stateMachineId, stream);
        return stateMachineId;
    }

    public boolean exists(String stateMachineId) {
        return ds.exists(stateMachineId);
    }

    public String create(InputStream stream) {
        UUID uuid = UUID.randomUUID();
        String stateMachineId = uuid.toString();
        this.create(stateMachineId, stream);
        return stateMachineId;
    }

    public Set<String> stateMachineNames() {
        return this.ds.stateMachineNames();
    }

}
