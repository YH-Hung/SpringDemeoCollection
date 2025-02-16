package hle.metricscollection.config;

import hle.metricscollection.handler.ApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class WebConfig {
    @Bean
    public RouterFunction<ServerResponse> routes(ApiHandler apiHandler) {
        return route()
                .GET("/test/delay", apiHandler::paleResponse)
                .GET("/test/db", apiHandler::queryDb)
                .build();
    }
}
