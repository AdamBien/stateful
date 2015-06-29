package com.airhacks.stateful.machine.entity;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.commons.scxml2.model.SCXML;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class StateMachineIT {

    private EntityManager em;
    private EntityTransaction tx;

    @Before
    public void initEM() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("it");
        this.em = emf.createEntityManager();
        this.tx = this.em.getTransaction();
    }

    @Test
    public void crud() throws ModelException, IOException, XMLStreamException {
        String id = "machine-" + System.currentTimeMillis();
        SCXML initialState = SCXMLReader.read(this.getClass().getResourceAsStream("/state.xml"));
        SCXMLExecutor initial = new SCXMLExecutor();
        initial.setStateMachine(initialState);
        initial.go();

        StateMachine stateMachine = new StateMachine(id, initial);
        this.tx.begin();
        this.em.merge(stateMachine);
        this.tx.commit();

        StateMachine found = this.em.find(StateMachine.class, id);
        SCXMLExecutor actual = found.getSCXMLExecutor();

        SCXML actualState = actual.getStateMachine();
        assertThat(initialState.getName(), is(actualState.getName()));
    }

}
