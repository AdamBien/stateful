package com.airhacks.stateful.definition.control;

import com.airhacks.stateful.definition.control.DefinitionStore;
import java.io.InputStream;
import org.apache.commons.scxml2.model.SCXML;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
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
        InputStream stream = this.getClass().getResourceAsStream("/state.xml");
        SCXML created = this.cut.createStateMachine(slot, stream);
        assertNotNull(created);
        assertTrue(this.cut.exists(slot));
        SCXML fetched = this.cut.get(slot);
        assertThat(fetched, is(created));

        this.cut.remove(slot);

        assertFalse(this.cut.exists(slot));
        assertNull(this.cut.get(slot));

    }

}
