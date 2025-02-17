package org.hle.resilience4jcookbook.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hle.resilience4jcookbook.dto.JobRequest;
import org.hle.resilience4jcookbook.dto.TriggerResult;
import org.hle.resilience4jcookbook.model.BulkheadReject;
import org.hle.resilience4jcookbook.model.JobComplete;
import org.hle.resilience4jcookbook.model.JobFail;
import org.hle.resilience4jcookbook.model.TimeoutAndCancel;
import org.hle.resilience4jcookbook.service.JobDispatcher;
import org.hle.resilience4jcookbook.service.JobHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JobDispatcherImpl implements JobDispatcher {

    private final JobHandler jobHandler;

    public JobDispatcherImpl(JobHandler jobHandler) {
        this.jobHandler = jobHandler;
    }

    @Override
    public TriggerResult dispatch(JobRequest jobRequest) {
        var job = jobHandler.handle(jobRequest);
        job.whenComplete((r, t) -> {
            if (t != null) {
                log.error("Job complete with exception: {}", t.getMessage());
            } else if (r != null) {
                log.info("Job Result: {}", r);
            }
        }).thenAccept(r -> {
            log.info("Job completed, do some post processing... ");
        });

        if (job.isDone()) {
            log.warn("Job is done before return trigger waiting result");
            var result = job.join();
            return switch (result) {
                case BulkheadReject bulkheadReject -> new TriggerResult(false, "Exceed concurrent limit");
                case JobComplete jobComplete -> new TriggerResult(true, "Triggered and completed");
                case JobFail jobFail -> new TriggerResult(true, "Triggered but failed");
                case TimeoutAndCancel timeoutAndCancel -> new TriggerResult(true, "Triggerred but timeout");
            };
        }

        return new TriggerResult(true, "Triggered, waiting...");
    }
}
