package com.airhacks.stateful.machine.boundary;

import java.io.IOException;
import javax.json.Json;
import javax.json.JsonObject;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class AttachmentManagerTest {

    AttachmentManager cut;

    @Before
    public void init() {
        this.cut = new AttachmentManager();
    }

    @Test
    public void reserialize() throws IOException {
        JsonObject expected = Json.createObjectBuilder().
                add("name", "duke").
                build();
        byte[] serialized = this.cut.serialize(expected);
        JsonObject actual = this.cut.deserialize(serialized);
        assertNotSame(actual, expected);
        assertThat(actual, is(expected));
    }

}
