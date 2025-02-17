package org.hle.resilience4jcookbook.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class WsResponse {
    private ZonedDateTime date;
    private Integer temperatureC;
    private Double temperatureF;
    private String summary;
}
