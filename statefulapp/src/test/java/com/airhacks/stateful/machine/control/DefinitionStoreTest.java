package com.airhacks.stateful.machine.control;

import com.airhacks.stateful.machine.control.DefinitionStore;
import org.apache.commons.scxml2.SCXMLExecutor;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class DefinitionStoreTest {

    DefinitionStore cut;

    @Before
    public void init() {
        this.cut = new DefinitionStore();
        this.cut.init();
    }

    @Test
    public void crudStateMachine() throws Exception {
        final String slot = "test";

        this.cut.store(slot, new SCXMLExecutor());

        assertTrue(this.cut.exists(slot));

        SCXMLExecutor fetched = this.cut.get(slot);
        assertFalse(fetched.isRunning());

        this.cut.remove(slot);
        assertFalse(fetched.isRunning());

        assertFalse(this.cut.exists(slot));
        assertNull(this.cut.get(slot));

    }

}
