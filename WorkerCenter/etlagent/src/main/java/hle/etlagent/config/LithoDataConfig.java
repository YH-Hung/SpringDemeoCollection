package hle.etlagent.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class LithoDataConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.litho")
    public DataSourceProperties lithoDataSourceProperties() {
        return new DataSourceProperties();
    }

    // Inject credential before call build()
    @Bean
    public DataSource lithoDataSource() {
        return lithoDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean("lithoTxManager")
    public DataSourceTransactionManager lithoTxManager() {
        return new DataSourceTransactionManager(lithoDataSource());
    }

    @Bean("lithoNamedJdbcTemplate")
    public NamedParameterJdbcTemplate lithoNamedJdbcTemplate() {
        return new NamedParameterJdbcTemplate(lithoDataSource());
    }
}
