package hle.etlagent;

import eu.rekawek.toxiproxy.Proxy;
import eu.rekawek.toxiproxy.ToxiproxyClient;
import hle.etlagent.dao.ProductWorkflowAdoptRepo;
import hle.etlagent.model.TimeWindow;
import hle.etlagent.service.InspectProcessor;
import hle.etlagent.simulator.ReEnableProxy;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.ToxiproxyContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Timer;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RunTimeErrorHandleTests {

    private static final Network network = Network.newNetwork();

    @SuppressWarnings("resource")
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15.4-alpine"
    ).withInitScript("initialize_postgres.sql")
            .withNetwork(network)
            .withNetworkAliases("postgres");

    static Proxy postgresqlProxy;

    @Container
    static final ToxiproxyContainer toxiproxy = new ToxiproxyContainer("ghcr.io/shopify/toxiproxy:2.5.0")
            .withNetwork(network);

    @SuppressWarnings("resource")
    @Container
    static MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:lts")
            .withInitScript("initialize_mariadb.sql")
            .withNetwork(network)
            .withNetworkAliases("maria");

    static Proxy mariadbProxy;

    @DynamicPropertySource  // non-auto-config datasource is not compatible with Spring boot 3.1 @ServiceConnection
    static void configureProperties(DynamicPropertyRegistry registry) throws IOException {
        var toxiproxyClient = new ToxiproxyClient(toxiproxy.getHost(), toxiproxy.getControlPort());
        postgresqlProxy = toxiproxyClient.createProxy("postgresql", "0.0.0.0:8666", "postgres:5432");
        mariadbProxy = toxiproxyClient.createProxy("mariadb", "0.0.0.0:8667", "maria:3306");

        registry.add("spring.datasource.inline.url", () -> "jdbc:postgresql://%s:%d/%s".formatted(toxiproxy.getHost(), toxiproxy.getMappedPort(8666), postgres.getDatabaseName()));
        registry.add("spring.datasource.inline.username", postgres::getUsername);
        registry.add("spring.datasource.inline.password", postgres::getPassword);
        registry.add("spring.datasource.litho.url", () -> "jdbc:mariadb://%s:%d/%s".formatted(toxiproxy.getHost(), toxiproxy.getMappedPort(8667), mariadb.getDatabaseName()));
        registry.add("spring.datasource.litho.username", mariadb::getUsername);
        registry.add("spring.datasource.litho.password", mariadb::getPassword);
    }

    @Autowired
    ProductWorkflowAdoptRepo adoptRepo;

    @Autowired
    InspectProcessor inspectProcessor;


    @Test
    @Order(1)
    void inlineTempDownCoverByRetry() throws IOException {
        postgresqlProxy.disable();

        var from = LocalDateTime.of(2023, 9, 12, 0, 0, 0);
        var to = LocalDateTime.of(2023, 9, 13, 0, 0, 0);

        Timer timer = new Timer();
        timer.schedule(new ReEnableProxy(postgresqlProxy), 30000);

        inspectProcessor.fetchInspRawByWindow(new TimeWindow(from, to));
        var result = adoptRepo.findByWindow(from, to);

        assertThat(result).hasSize(24);
    }

    @Test
    @Order(2)
    void lithoDbTempDownCoverByRetry() throws IOException {
        mariadbProxy.disable();

        var from = LocalDateTime.of(2023, 9, 12, 0, 0, 0);
        var to = LocalDateTime.of(2023, 9, 13, 0, 0, 0);

        Timer timer = new Timer();
        timer.schedule(new ReEnableProxy(mariadbProxy), 30000);

        var result = adoptRepo.findByWindow(from, to);

        assertThat(result).hasSize(24);
    }
}
