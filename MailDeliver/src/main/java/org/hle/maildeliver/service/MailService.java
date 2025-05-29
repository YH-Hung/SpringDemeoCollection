package org.hle.maildeliver.service;

import org.hle.maildeliver.dto.MailDto;

public interface MailService {
    void sendMail(MailDto mailDto);
}
