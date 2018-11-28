package org.faboo.blog.dynamicport.service;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.Retriever;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.Message;

import static com.icegreen.greenmail.util.ServerSetup.PROTOCOL_SMTP;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SomeServiceTest {

    @Value("${smtp.port}")
    private int port;

    @Value("${email.user}")
    private String user;

    @Value("${email.password}")
    private String password;

    private GreenMail smtpServer;

    @Autowired
    private SomeService sut;

    @Before
    public void setUp() {
        smtpServer = new GreenMail(new ServerSetup(port, null, PROTOCOL_SMTP));
        smtpServer.setUser(user, password);
        smtpServer.start();
    }

    @After
    public void after() {
        smtpServer.stop();
    }

    @Test
    public void emailShouldBeSend() {

        // prepare
        String toAddress = "receiver@test";
        String subject = "sending email from test";
        String body = "the body of our test email";
        // act
        sut.sendEmail(toAddress, subject, body);

        // expect
        Message[] receivedMessages = smtpServer.getReceivedMessages();
        Assert.assertEquals("only one email should be send", 1, receivedMessages.length);
        // test other aspects of the message ...
    }
}