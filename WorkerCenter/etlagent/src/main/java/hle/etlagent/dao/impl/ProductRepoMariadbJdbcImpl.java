package hle.etlagent.dao.impl;

import hle.etlagent.dao.ProductRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepoMariadbJdbcImpl implements ProductRepo {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductRepoMariadbJdbcImpl(@Qualifier("lithoNamedJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Integer> getIds() {
        String sql = """
                SELECT product_id FROM product
                """;

        return jdbcTemplate.queryForList(sql, new MapSqlParameterSource(), Integer.TYPE);
    }
}
