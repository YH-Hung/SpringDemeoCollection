package org.hle.resilience4jcookbook.controller;

import org.hle.resilience4jcookbook.dto.JobRequest;
import org.hle.resilience4jcookbook.dto.TriggerResult;
import org.hle.resilience4jcookbook.service.JobDispatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trigger")
public class TriggerController {

    private final JobDispatcher jobDispatcher;

    public TriggerController(JobDispatcher jobDispatcher) {
        this.jobDispatcher = jobDispatcher;
    }

    @PostMapping
    public ResponseEntity<TriggerResult> trigger(@RequestBody JobRequest jobRequest) {
        var triggerResult = jobDispatcher.dispatch(jobRequest);
        if (triggerResult.isSuccess()) {
            return ResponseEntity.ok(triggerResult);
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(triggerResult);
        }
    }
}
