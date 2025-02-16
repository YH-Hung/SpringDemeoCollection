package org.hle.springconsole.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "runner", name = "activated", matchIfMissing = true)
public class EmptyJob implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.warn("Please assign the job name...");
    }
}
