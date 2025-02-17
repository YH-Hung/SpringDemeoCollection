package org.hle.resilience4jcookbook.service;

import org.hle.resilience4jcookbook.dto.JobRequest;
import org.hle.resilience4jcookbook.model.JobResult;

import java.util.concurrent.CompletableFuture;

public interface JobHandler {
    CompletableFuture<JobResult> handle(JobRequest jobRequest);
}
