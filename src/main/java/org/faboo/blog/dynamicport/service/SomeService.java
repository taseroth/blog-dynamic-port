package org.faboo.blog.dynamicport.service;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


public class SomeService {

    private final static Logger log = LoggerFactory.getLogger(SomeService.class);

    private JavaMailSender mailSender;
    private String fromAddress;


    public SomeService(JavaMailSender mailSender, String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    public void sendEmail(String toAddress, String subject, String body) {

        log.info(String.format("sending email (subject: '%s')", subject));
        MimeMessage email = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(email, false);
            helper.setFrom(fromAddress);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(body);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException | MailException e) {
            log.error("error sending email:", e);
            // in an real world service, we would probably do so handling and / or exception mapping
        }
    }

}
