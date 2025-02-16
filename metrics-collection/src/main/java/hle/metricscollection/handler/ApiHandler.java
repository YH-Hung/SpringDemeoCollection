package hle.metricscollection.handler;

import hle.metricscollection.dao.ProductInspectRepository;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ApiHandler {

    private final ProductInspectRepository repository;

    public ApiHandler(ProductInspectRepository repository) {
        this.repository = repository;
    }

    @Timed(value = "test.delay.time", description = "Time taken of delay")
    public ServerResponse paleResponse(ServerRequest request) {
        try (ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor()) {
            int randomDelayS = ThreadLocalRandom.current().nextInt(1, 10);
            executorService.schedule(() -> {
                log.info("Delay {} seconds finish...", randomDelayS);
            }, randomDelayS, TimeUnit.SECONDS);
        }

        return ServerResponse.noContent().build();
    }

    public ServerResponse queryDb(ServerRequest request) {
        return ServerResponse.ok().body(repository.findAll());
    }
}
