/**
 * 
 */
package io.nats.client;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author larry
 *
 */
@Category(UnitTest.class)
public class ServerInfoTest {
    final Logger logger = LoggerFactory.getLogger(ServerInfoTest.class);

    @Rule
    public TestCasePrinterRule pr = new TestCasePrinterRule(System.out);

    static String testString =
            "INFO {\"server_id\":\"s76hOxUCzhR2ngkcVYSPPV\",\"version\":\"0.9.4\",\"go\":\"go1.6.3\",\"host\":\"0.0.0.0\",\"port\":4222,\"auth_required\":true,\"ssl_required\":true,\"tls_required\":true,\"tls_verify\":false,\"max_payload\":1048576,\"connect_urls\":[\"10.0.1.3:4222\",\"[fe80::42:aff:fe00:103]:4222\"]}\r\n";
    static ServerInfo testInstance = ServerInfo.createFromWire(testString);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    // /**
    // * Test method for {@link io.nats.client.ServerInfo#ServerInfo(java.lang.String)}.
    // */
    // @Test
    // public void testServerInfo() {
    // fail("Not yet implemented"); // TODO
    // }

    /**
     * Test method for {@link io.nats.client.ServerInfo#getId()}.
     */
    @Test
    public void testGetId() {
        assertEquals("s76hOxUCzhR2ngkcVYSPPV", testInstance.getId());
    }

    /**
     * Test method for {@link io.nats.client.ServerInfo#getHost()}.
     */
    @Test
    public void testGetHost() {
        assertEquals("0.0.0.0", testInstance.getHost());
    }

    /**
     * Test method for {@link io.nats.client.ServerInfo#getPort()}.
     */
    @Test
    public void testGetPort() {
        assertEquals(4222, testInstance.getPort());
    }

    /**
     * Test method for {@link io.nats.client.ServerInfo#getVersion()}.
     */
    @Test
    public void testGetVersion() {
        assertEquals("0.9.4", testInstance.getVersion());
    }

    /**
     * Test method for {@link io.nats.client.ServerInfo#isAuthRequired()}.
     */
    @Test
    public void testIsAuthRequired() {
        assertEquals(true, testInstance.isAuthRequired());
    }

    /**
     * Test method for {@link io.nats.client.ServerInfo#isTlsRequired()}.
     */
    @Test
    public void testIsTlsRequired() {
        assertEquals(true, testInstance.isTlsRequired());
    }

    /**
     * Test method for {@link io.nats.client.ServerInfo#getMaxPayload()}.
     */
    @Test
    public void testGetMaxPayload() {
        assertEquals(1048576, testInstance.getMaxPayload());
    }

    /**
     * Test method for {@link io.nats.client.ServerInfo#toString()}.
     */
    @Test
    public void testToString() {
        String outputString = testInstance.toString();
        assertEquals(testString.trim(), outputString);
    }
}
