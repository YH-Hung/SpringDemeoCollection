package hle.etlagent;

import hle.etlagent.dao.ProductInspRepo;
import hle.etlagent.dao.ProductRepo;
import hle.etlagent.dao.ProductWorkflowAdoptRepo;
import hle.etlagent.model.TimeWindow;
import hle.etlagent.service.InspectProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("resource")
@Testcontainers // @Testcontainers + @Container = start before all + stop after all
@SpringBootTest
class EtlagentApplicationTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15.4-alpine"
    ).withInitScript("initialize_postgres.sql");

    @Container
    static MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:lts")
            .withInitScript("initialize_mariadb.sql");

    @DynamicPropertySource  // non-auto-config datasource is not compatible with Spring boot 3.1 @ServiceConnection
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.inline.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.inline.username", postgres::getUsername);
        registry.add("spring.datasource.inline.password", postgres::getPassword);
        registry.add("spring.datasource.litho.url", mariadb::getJdbcUrl);
        registry.add("spring.datasource.litho.username", mariadb::getUsername);
        registry.add("spring.datasource.litho.password", mariadb::getPassword);
    }

    @Autowired
    ProductInspRepo inspRepo;

    @Autowired
    ProductWorkflowAdoptRepo adoptRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    InspectProcessor inspectProcessor;

    @Test
    void contextLoads() {
    }

    @Test
    void inlineDbConnects() {
        var result = inspRepo.findAll();
        assertThat(result).hasSize(40);
    }

    @Test
    void lithoDbConnects() {
        var result = productRepo.getIds();
        assertThat(result).hasSize(10);
    }

    @Test
    void inlineToLithoFetch() {
        var from = LocalDateTime.of(2023, 9, 12, 0, 0, 0);
        var to = LocalDateTime.of(2023, 9, 13, 0, 0, 0);
        inspectProcessor.fetchInspRawByWindow(new TimeWindow(from, to));
        var result = adoptRepo.findByWindow(from, to);

        assertThat(result).hasSize(24);
    }

}

