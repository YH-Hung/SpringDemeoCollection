package org.hle.maildeliver;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@TestConfiguration
class TestcontainersConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public MailpitContainer mailpitContainer() {
        return new MailpitContainer();
    }

    @Bean
    public JavaMailSenderImpl javaMailSender(MailpitContainer mailpitContainer) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailpitContainer.getHost());
        mailSender.setPort(mailpitContainer.getSmtpPort());
        return mailSender;
    }
}
