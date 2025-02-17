package org.hle.resilience4jcookbook.dto;

public record JobRequest(Long takeMs, String name) {
}
