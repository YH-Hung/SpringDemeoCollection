package org.hle.resilience4jcookbook.model;

public sealed interface JobResult permits BulkheadReject, JobComplete, JobFail, TimeoutAndCancel {
    String message();
}
