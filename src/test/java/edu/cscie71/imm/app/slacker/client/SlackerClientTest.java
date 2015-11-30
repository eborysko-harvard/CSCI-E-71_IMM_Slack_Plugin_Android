package edu.cscie71.imm.app.slacker.client;

import org.junit.*;
import com.github.restdriver.clientdriver.ClientDriverRequest.Method;
import com.github.restdriver.clientdriver.ClientDriverRule;

import static com.github.restdriver.clientdriver.RestClientDriver.*;

public class SlackerClientTest {

    ISlackerClient mockSlack;
    String token = "xoxp-10020492535-10036686290-14227963249-1cb545e1ae";
    String immTestChannel = "C0F6U0R5E";
    String message = "This is the Android test.";

    @Rule
    public ClientDriverRule clientDriver = new ClientDriverRule();

    @Before
    public void setUp() throws Exception {
        mockSlack = new SlackerClient(clientDriver.getBaseUrl());
    }

    @Test
    public void testResponseIsOkAndContainsMessage() throws Exception {
        clientDriver.addExpectation(
                onRequestTo("/chat.postMessage").withMethod(Method.POST)
                        .withParam("as_user", "true")
                        .withParam("token", token)
                        .withParam("channel", immTestChannel)
                        .withParam("text", message),
                giveResponse("\"ok\":true,\"text\":\"This is the Android test.\"", "text/plain")
        );
        String okMsgResponse = mockSlack.postMessage(token, immTestChannel, message);
        Assert.assertTrue(okMsgResponse.contains("\"ok\":true"));
        Assert.assertTrue(okMsgResponse.contains("\"text\":\"This is the Android test.\""));
    }

    @Test
    public void testGetUserInfo() throws Exception {
        clientDriver.addExpectation(
                onRequestTo("/api/users.info").withMethod(Method.GET)
                        .withParam("token", token)
                        .withParam("user", "U0A12L68J"),
                giveResponse("\"ok\":true,\"real_name\":\"Jeffry Pincus\"", "text/plain")
        );
        String userInfo = mockSlack.getUserInfo(token, "U0A12L68J");
        Assert.assertTrue(userInfo.contains("\"ok\":true"));
        Assert.assertTrue(userInfo.contains("\"real_name\":\"Jeffry Pincus\""));
    }

    @After
    public void tearDown() throws Exception {
        mockSlack = null;
    }
}
