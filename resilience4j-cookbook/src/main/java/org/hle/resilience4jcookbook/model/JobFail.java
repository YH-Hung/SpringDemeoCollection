package org.hle.resilience4jcookbook.model;

public record JobFail(String message) implements JobResult {
}
