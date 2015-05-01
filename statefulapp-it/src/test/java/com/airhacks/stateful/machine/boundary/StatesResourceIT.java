package com.airhacks.stateful.machine.boundary;

import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import org.junit.Rule;

/**
 *
 * @author airhacks.com
 */
public class StatesResourceIT {

    @Rule
    public JAXRSClientProvider builder = JAXRSClientProvider.buildWithURI("http://localhost:8080/statefulapp/resources/states");

    public StatesResourceIT() {
    }

}
