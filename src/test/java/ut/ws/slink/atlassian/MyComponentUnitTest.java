package ut.ws.slink.atlassian;

import org.junit.Test;
import ws.slink.atlassian.api.MyPluginComponent;
import ws.slink.atlassian.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}