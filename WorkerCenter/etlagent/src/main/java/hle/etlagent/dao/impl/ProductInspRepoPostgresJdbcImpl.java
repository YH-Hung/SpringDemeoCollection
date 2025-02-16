package hle.etlagent.dao.impl;

import hle.etlagent.dao.ProductInspRepo;
import hle.etlagent.dao.impl.rowmapper.ProductInspectRowMapper;
import hle.etlagent.dto.ProductInspReq;
import hle.etlagent.model.ProductInspect;
import hle.etlagent.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ProductInspRepoPostgresJdbcImpl implements ProductInspRepo {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductInspRepoPostgresJdbcImpl(@Qualifier("inlineNamedJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ProductInspect> findAll() {
        String sql = """
                SELECT uid, product_id, inspect_date, create_date, last_modified_date
                FROM product_inspect
                """;

        return jdbcTemplate.query(sql, new ProductInspectRowMapper());
    }

    @Override
    public List<ProductInspect> findBetween(LocalDateTime from, LocalDateTime to) {
        String sql = """
                SELECT uid, product_id, inspect_date, create_date, last_modified_date
                FROM product_inspect
                WHERE inspect_date BETWEEN :fromDt AND :toDt
                """;

        var params = new MapSqlParameterSource()
                .addValue("fromDt", from)
                .addValue("toDt", to);

        return jdbcTemplate.query(sql, params, new ProductInspectRowMapper());
    }

    @Override
    public void createNewInsp(ProductInspReq inspReq) {
        String sql = """
                INSERT INTO product_inspect (product_id, inspect_date, create_date, last_modified_date)
                VALUES (:productId, :inspectDt, :createDt, :lastModifiedDt)
                """;

        var params = new MapSqlParameterSource()
                .addValue("productId", inspReq.getProductId())
                .addValue("inspectDt", DateTimeUtil.parseDashDateTime(inspReq.getInspDate()))
                .addValue("createDt", LocalDateTime.now())
                .addValue("lastModifiedDt", LocalDateTime.now());

        jdbcTemplate.update(sql, params);
    }
}
