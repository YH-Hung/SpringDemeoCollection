package org.hle.resilience4jcookbook.controller;

import org.hle.resilience4jcookbook.dto.WsResponse;
import org.hle.resilience4jcookbook.ws.WsClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ws")
public class WebServiceController {

    private final WsClient wsClient;

    public WebServiceController(WsClient wsClient) {
        this.wsClient = wsClient;
    }

    @GetMapping
    public List<WsResponse> callWs() {
        return wsClient.getWeather();
    }

    @GetMapping("/fake")
    public List<WsResponse> callWsFake() {
        return wsClient.getWeatherFake();
    }
}
