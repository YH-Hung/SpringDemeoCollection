package org.hle.maildeliver.router;

import org.hle.maildeliver.handler.MailHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class MailRouter {

    @Bean
    public RouterFunction<ServerResponse> mailRoute(MailHandler mailHandler) {
        return RouterFunctions
                .route(POST("/api/mail").and(accept(MediaType.APPLICATION_JSON)), mailHandler::sendMail);
    }
}