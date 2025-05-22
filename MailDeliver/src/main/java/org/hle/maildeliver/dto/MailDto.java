package org.hle.maildeliver.dto;

import lombok.Data;

@Data
public class MailDto {
    private String from;
    private String to;
    // Optional field
    private String cc;
    private String subject;
    private String text;
}
