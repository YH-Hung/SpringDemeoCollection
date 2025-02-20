package hle.etlagent.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        basePackages = "hle.etlagent.dao.jpa.inline",
        entityManagerFactoryRef = "inlineEntityManagerFactory",
        transactionManagerRef = "inlineJpaTxManager"
)
public class InlineDataConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.inline")
    public DataSourceProperties inlineDataSourceProperties() {
        return new DataSourceProperties();
    }

    // Inject credential before call build()
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.inline.configuration")
    public HikariDataSource inlineDataSource() {
        return inlineDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "inlineEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean inlineEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(inlineDataSource())
                .packages("hle.etlagent.entity.inline").build();
    }

    @Bean(name = "inlineJpaTxManager")
    public JpaTransactionManager inlineJpaTxManager(@Qualifier("inlineEntityManagerFactory") LocalContainerEntityManagerFactoryBean inlineEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(inlineEntityManagerFactory.getObject()));
    }

    @Bean(name = "inlineTxManager")
    public DataSourceTransactionManager inlineTxManager() {
        return new DataSourceTransactionManager(inlineDataSource());
    }

    @Bean(name = "inlineNamedJdbcTemplate")
    public NamedParameterJdbcTemplate inlineNamedJdbcTemplate() {
        return new NamedParameterJdbcTemplate(inlineDataSource());
    }
}
