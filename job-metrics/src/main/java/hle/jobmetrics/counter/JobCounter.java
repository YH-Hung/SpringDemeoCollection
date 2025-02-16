package hle.jobmetrics.counter;

import io.micrometer.core.annotation.Counted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobCounter {
    @Counted(value = "job-long-running-count", description = "counter of job running", extraTags = {"job-name", "job1"})
    public void countLongRunning() {
        log.info("Long running job...");
    }
}
