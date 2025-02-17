package org.hle.resilience4jcookbook.model;

public record BulkheadReject(String message) implements JobResult {
}
