package org.hle.resilience4jcookbook.service;

import org.hle.resilience4jcookbook.dto.JobRequest;
import org.hle.resilience4jcookbook.dto.TriggerResult;

public interface JobDispatcher {
    TriggerResult dispatch(JobRequest jobRequest);
}
