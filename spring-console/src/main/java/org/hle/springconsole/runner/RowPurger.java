package org.hle.springconsole.runner;

import lombok.extern.slf4j.Slf4j;
import org.hle.springconsole.config.RunnerConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "runner", name = "activated", havingValue = "row-purger")
public class RowPurger implements CommandLineRunner {

    private final RunnerConfig config;

    public RowPurger(RunnerConfig config) {
        this.config = config;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Purging rows... batch size={}", config.getBatchSize());
    }
}
