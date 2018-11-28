package org.faboo.blog.dynamicport;

import org.faboo.blog.dynamicport.service.SomeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@SpringBootApplication
public class DynamicPortApplication {

    @Value("${smtp.host}")
    private String host;

    @Value("${smtp.debug}")
    private String debugEnabled;

    @Value("${smtp.port}")
    private Integer port;

    @Value("${email.user}")
    private String user;

    @Value("${email.password}")
    private String password;

    @Value("${email.from}")
    private String emailFrom;


    public static void main(String[] args) {
        SpringApplication.run(DynamicPortApplication.class, args);
    }

    @Bean
    public SomeService someService() {
        return new SomeService(mailSender(), emailFrom);
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setUsername(user);
        javaMailSender.setPassword(password);
        if (port != null) {
            javaMailSender.setPort(port);
        }
        Properties mailProperties = javaMailSender.getJavaMailProperties();
        mailProperties.put("mail.transport.protocol", "smtp");
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.starttls.enable", "true");
        mailProperties.put("mail.debug", debugEnabled);

        // 1 Minute timeout. Default is unlimited
        mailProperties.put("mail.smtp.connectiontimeout", 1000 * 60);

        return javaMailSender;
    }

}
