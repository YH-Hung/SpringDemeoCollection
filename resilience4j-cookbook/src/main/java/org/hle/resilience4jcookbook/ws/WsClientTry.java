package org.hle.resilience4jcookbook.ws;

import org.hle.resilience4jcookbook.dto.WsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WsClientTry {

    private final WsClient wsClient;

    public WsClientTry(WsClient wsClient) {
        this.wsClient = wsClient;
    }

}
