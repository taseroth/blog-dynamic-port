package org.faboo.blog.dynamicport.service;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.util.SocketUtils;

import javax.mail.internet.MimeMessage;

import static com.icegreen.greenmail.util.ServerSetup.PROTOCOL_SMTP;

/**
 * An @Rule that provides an GreenMail SMTP server running on localhost with a random dynamic port.
 * The port the server is running on is injected into the Context as "smtp.port" and can be used from other
 * components.
 */
@Component
@ContextConfiguration(initializers = { SmtpServerRule.RandomPortInitializer.class})
public class SmtpServerRule extends ExternalResource {

    private static final Logger log = LoggerFactory.getLogger(SmtpServerRule.class);
    @Value("${smtp.port}")
    private int port;

    @Value("${email.user}")
    private String user;

    @Value("${email.password}")
    private String password;

    private GreenMail smtpServer;

    @Override
    protected void before() {
        log.info("starting Greenmail on port: {}", port);
        smtpServer = new GreenMail(new ServerSetup(port, null, PROTOCOL_SMTP));
        smtpServer.setUser(user, password);
        smtpServer.start();
    }

    @Override
    protected void after() {
        log.info("stopping greenmail server");
        smtpServer.stop();
    }

    public MimeMessage[] getMessages() {
        return smtpServer.getReceivedMessages();
    }

    public static class RandomPortInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

            int randomPort = SocketUtils.findAvailableTcpPort();
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                    "smtp.port=" + randomPort);
        }
    }
}
