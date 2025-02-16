package hle.jobmetrics.job;

import hle.jobmetrics.counter.JobCounter;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class DoSomethingJob {

    private final AtomicInteger isRunning = new AtomicInteger(0);
    private final JobCounter jobCounter;

    public DoSomethingJob(MeterRegistry registry, JobCounter jobCounter) {
        this.jobCounter = jobCounter;
        Gauge.builder("job-running-status", isRunning, AtomicInteger::get)
                .description("running: 1 / not running: 0")
                .tag("job-name", "job1")
                .register(registry);
    }

    @Timed(value = "job-running-time", description = "time distribution of running", extraTags = {"job-name", "job1"}, histogram = true)
    @Scheduled(fixedDelay = 10_000)
    public void doSomething() throws InterruptedException {
        log.info("Start do something...");
        isRunning.set(1);
        int sleepSeconds = ThreadLocalRandom.current().nextInt(3, 12);
        if (sleepSeconds > 7) {
            jobCounter.countLongRunning();
        }
        Thread.sleep(sleepSeconds * 1000L);
        isRunning.set(0);
        log.info("End do something...");
    }


}
