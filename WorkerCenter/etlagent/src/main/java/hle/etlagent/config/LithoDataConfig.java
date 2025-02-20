package hle.etlagent.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "hle.etlagent.dao.jpa.litho",
        entityManagerFactoryRef = "lithoEntityManagerFactory",
        transactionManagerRef = "lithoJpaTxManager"
)
public class LithoDataConfig {

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.litho")
    public DataSourceProperties lithoDataSourceProperties() {
        return new DataSourceProperties();
    }

    // Inject credential before call build()
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.litho.configuration")
    public HikariDataSource lithoDataSource() {
        return lithoDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "lithoEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean lithoEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(lithoDataSource())
                .packages("hle.etlagent.entity.litho").build();
    }

    @Primary
    @Bean(name = "lithoJpaTxManager")
    public JpaTransactionManager lithoJpaTxManager(@Qualifier("lithoEntityManagerFactory") LocalContainerEntityManagerFactoryBean lithoEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(lithoEntityManagerFactory.getObject()));
    }

    @Bean(name = "lithoTxManager")
    public DataSourceTransactionManager lithoTxManager() {
        return new DataSourceTransactionManager(lithoDataSource());
    }

    @Bean(name = "lithoNamedJdbcTemplate")
    public NamedParameterJdbcTemplate lithoNamedJdbcTemplate() {
        return new NamedParameterJdbcTemplate(lithoDataSource());
    }
}
