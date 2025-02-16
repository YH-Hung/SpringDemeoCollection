package org.hle.springconsole.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "runner", name = "activated", havingValue = "table-purger")
public class TablePurger implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("Purging tables...");
    }
}
