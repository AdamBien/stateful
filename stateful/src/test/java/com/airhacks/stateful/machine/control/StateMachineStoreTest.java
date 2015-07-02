package com.airhacks.stateful.machine.control;

import com.airhacks.rulz.em.EntityManagerProvider;
import com.airhacks.stateful.TestSCXMLExecutorProvider;
import javax.persistence.EntityTransaction;
import org.apache.commons.scxml2.SCXMLExecutor;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class DefinitionStoreTest {

    DefinitionStore cut;

    @Rule
    public EntityManagerProvider provider
            = EntityManagerProvider.persistenceUnit("it");
    private EntityTransaction tx;

    @Before
    public void init() {
        this.cut = new DefinitionStore();
        this.cut.em = provider.em();
        this.tx = provider.tx();

    }

    @Test
    public void crudStateMachine() throws Exception {
        final String slot = "test";
        this.tx.begin();
        final SCXMLExecutor scxmlExecutor = TestSCXMLExecutorProvider.create();
        this.cut.store(slot, scxmlExecutor);
        this.tx.commit();

        assertTrue(this.cut.exists(slot));

        SCXMLExecutor fetched = this.cut.find(slot);
        assertNotNull(fetched.getStateMachine());

        this.tx.begin();
        this.cut.remove(slot);
        this.tx.commit();

        this.cut.em.clear();

        assertFalse(this.cut.exists(slot));
        assertNull(this.cut.find(slot));

    }

}
