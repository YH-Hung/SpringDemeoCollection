package org.hle.resilience4jcookbook;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

import java.util.Timer;

@Slf4j
@SpringBootApplication
@EnableFeignClients
public class Resilience4jCookbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(Resilience4jCookbookApplication.class, args);
    }

    @Autowired
    BulkheadRegistry bulkheadRegistry;

    @EventListener(ContextClosedEvent.class)
    public void handleContextClosed() {
        log.info("Context closing..., do some action");
    }

    @PreDestroy
    public void waitBeforeShutdown() {
        Bulkhead bulkhead = bulkheadRegistry.bulkhead("job-handle");
        int waitingCount = 0;
        boolean stillRunning;

        log.warn("Before shutdown, check running jobs...");
        // TimeLimiter did not work here, must force shutdown after long polling.
//        do {
//            int available = bulkhead.getMetrics().getAvailableConcurrentCalls();
//            int maximum = bulkhead.getMetrics().getMaxAllowedConcurrentCalls();
//            stillRunning = available < maximum;
//            waitingCount++;
//            log.info("Waiting for {} concurrent calls... {} times", maximum - available, waitingCount);
//
//            // Even all bulkhead released, There might still some chained actions.
//            try {
//                Thread.sleep(10 * 1000);
//            } catch (InterruptedException e) {
//                log.error(e.getMessage(), e);
//            }
//        } while (stillRunning && waitingCount < 10);

        log.warn("shutdown...");
    }
}
