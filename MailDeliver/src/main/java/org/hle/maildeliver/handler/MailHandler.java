package org.hle.maildeliver.handler;

import org.hle.maildeliver.dto.MailDto;
import org.hle.maildeliver.service.MailService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class MailHandler {

    private final MailService mailService;

    public MailHandler(MailService mailService) {
        this.mailService = mailService;
    }

    public Mono<ServerResponse> sendMail(ServerRequest request) {
        return request.bodyToMono(MailDto.class)
                .doOnNext(mailService::sendMail)
                .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue("Email sent successfully"));
    }
}