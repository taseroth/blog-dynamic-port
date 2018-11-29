package org.faboo.blog.dynamicport.service;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.Message;

@RunWith(SpringRunner.class)
@SpringBootTest

public class SomeServiceTest {

    @Autowired
    private SomeService sut;

    @Autowired
    @Rule
    public SmtpServerRule smtpServerRule = new SmtpServerRule();

    @Test
    public void emailShouldBeSend() {

        // prepare
        String toAddress = "receiver@test";
        String subject = "sending email from test";
        String body = "the body of our test email";
        // act
        sut.sendEmail(toAddress, subject, body);

        // expect
        Message[] receivedMessages = smtpServerRule.getMessages();
        Assert.assertEquals("only one email should be send", 1, receivedMessages.length);
        // test other aspects of the message ...
    }

}