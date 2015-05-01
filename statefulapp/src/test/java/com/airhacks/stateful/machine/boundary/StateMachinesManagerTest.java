package com.airhacks.stateful.machine.boundary;

import com.airhacks.stateful.machine.boundary.StateMachinesManager;
import java.io.InputStream;
import org.apache.commons.scxml2.SCXMLExecutor;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class StateMachinesManagerTest {

    StateMachinesManager cut;

    @Before
    public void init() {
        this.cut = new StateMachinesManager();
    }

    @Test
    public void createAndStart() {
        final String slot = "test";
        InputStream stream = this.getClass().getResourceAsStream("/state.xml");
        SCXMLExecutor executor = this.cut.createAndStart(slot, stream);
        assertNotNull(executor);
        assertTrue(executor.isRunning());
    }

}
