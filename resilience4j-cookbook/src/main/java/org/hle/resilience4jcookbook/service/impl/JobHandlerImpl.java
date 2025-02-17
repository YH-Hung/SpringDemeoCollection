package org.hle.resilience4jcookbook.service.impl;


import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.hle.resilience4jcookbook.dto.JobRequest;
import org.hle.resilience4jcookbook.model.*;
import org.hle.resilience4jcookbook.service.JobHandler;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class JobHandlerImpl implements JobHandler {
    @Override
    @Bulkhead(name = "job-handle", fallbackMethod = "fallback")
    @TimeLimiter(name = "job-handle")   // TimeoutException
    public CompletableFuture<JobResult> handle(JobRequest jobRequest) {
        log.info("Job started, will take {} ms", jobRequest.takeMs());
        // TODO: Use virtual thread executor service
        // https://narasivenkat.medium.com/using-java-virtuals-threads-for-asynchronous-programming-29a3274e6294
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(jobRequest.takeMs());
                log.info("Job finished.");
                return new JobComplete("Complete without any constrain");
            } catch (InterruptedException e) {
                log.error("Job interrupted", e);
                return new JobFail(e.getMessage());
            }
        });
    }

    private CompletableFuture<JobResult> fallback(JobRequest jobRequest, BulkheadFullException ex) {
        log.warn("Bulkhead is full, request was rejected");
        return CompletableFuture.completedFuture(new BulkheadReject(ex.getMessage()));
    }

}
