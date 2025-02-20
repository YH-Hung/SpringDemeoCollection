package hle.etlagent;

import hle.etlagent.dao.jpa.inline.ProductInspectRepository;
import hle.etlagent.dao.jpa.litho.ProductRepository;
import hle.etlagent.dao.jpa.litho.ProductWorkflowAdoptRepository;
import hle.etlagent.entity.litho.ProductWorkflowAdopt;
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

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("resource")
@Testcontainers // @Testcontainers + @Container = start before all + stop after all
@SpringBootTest
class EtlagentJpaApplicationTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:17.3-alpine"
    ).withInitScript("initialize_postgres.sql");

    @Container
    static MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:11.7.2")
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
    ProductInspectRepository inspRepo;

    @Autowired
    ProductWorkflowAdoptRepository adoptRepo;

    @Autowired
    ProductRepository productRepo;


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
        var result = productRepo.findAll();
        assertThat(result).hasSize(10);
    }

    @Test
    void inlineToLithoFetch() {
        var from = LocalDateTime.of(2023, 9, 12, 0, 0, 0);
        var to = LocalDateTime.of(2023, 9, 13, 0, 0, 0);
        var inspRaws = inspRepo.findAllByInspectDateBetween(from, to);
        var adopts = inspRaws.stream().map(insp -> {
            var entity = new ProductWorkflowAdopt();
            entity.setProduct(productRepo.findById(insp.getProductId()).get());
            entity.setInspectDate(insp.getInspectDate());
            entity.setCreateDate(LocalDateTime.now());
            entity.setLastModifiedDate(LocalDateTime.now());

            return entity;
        }).toList();

        var result = adoptRepo.saveAll(adopts);

        assertThat(result).hasSize(24);
    }

}

