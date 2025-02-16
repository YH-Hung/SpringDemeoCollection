package hle.etlagent.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class InlineDataConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.inline")
    public DataSourceProperties inlineDataSourceProperties() {
        return new DataSourceProperties();
    }

    // Inject credential before call build()
    @Bean
    public DataSource inlineDataSource() {
        return inlineDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean("inlineTxManager")
    public DataSourceTransactionManager inlineTxManager() {
        return new DataSourceTransactionManager(inlineDataSource());
    }

    @Bean("inlineNamedJdbcTemplate")
    public NamedParameterJdbcTemplate inlineNamedJdbcTemplate() {
        return new NamedParameterJdbcTemplate(inlineDataSource());
    }
}
