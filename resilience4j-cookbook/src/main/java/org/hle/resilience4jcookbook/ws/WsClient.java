package org.hle.resilience4jcookbook.ws;

import io.github.resilience4j.retry.annotation.Retry;
import org.hle.resilience4jcookbook.dto.WsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "some-web-service")
public interface WsClient {

    @Retry(name = "some-web-service")
    @GetMapping("/weatherforecast")
    List<WsResponse> getWeather();

    @Retry(name = "some-web-service")
    @GetMapping("/weatherforecast/fake")
    List<WsResponse> getWeatherFake();
}
