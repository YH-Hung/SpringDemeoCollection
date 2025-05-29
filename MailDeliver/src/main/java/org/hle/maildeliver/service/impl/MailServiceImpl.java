package org.hle.maildeliver.service.impl;

import lombok.SneakyThrows;
import org.hle.maildeliver.dto.MailDto;
import org.hle.maildeliver.service.MailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @SneakyThrows
    @Override
    public void sendMail(MailDto dto) {
        var mimeMsg = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMsg);
        helper.setFrom(dto.getFrom());
        helper.setTo(dto.getTo());
        if (dto.getCc() != null && !dto.getCc().isEmpty()) {
            helper.setCc(dto.getCc());
        }
        helper.setSubject(dto.getSubject());
        helper.setText(dto.getText());

        mailSender.send(mimeMsg);
    }
}
