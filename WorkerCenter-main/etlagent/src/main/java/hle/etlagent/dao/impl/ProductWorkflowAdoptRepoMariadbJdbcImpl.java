package hle.etlagent.dao.impl;

import hle.etlagent.dao.ProductWorkflowAdoptRepo;
import hle.etlagent.dao.impl.rowmapper.ProductWorkflowAdoptRowMapper;
import hle.etlagent.model.ProductInspect;
import hle.etlagent.model.ProductWorkflowAdopt;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ProductWorkflowAdoptRepoMariadbJdbcImpl implements ProductWorkflowAdoptRepo {

    final
    NamedParameterJdbcTemplate jdbcTemplate;

    public ProductWorkflowAdoptRepoMariadbJdbcImpl(@Qualifier("lithoNamedJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ProductWorkflowAdopt> findByWindow(LocalDateTime from, LocalDateTime to) {
        String sql = """
                SELECT product_id, inspect_date, adopt
                FROM product_workflow_adopt
                WHERE inspect_date BETWEEN :fromDt AND :toDt
                """;

        var params = new MapSqlParameterSource()
                .addValue("fromDt", from)
                .addValue("toDt", to);

        return jdbcTemplate.query(sql, params, new ProductWorkflowAdoptRowMapper());
    }

    @Override
    public void saveRaws(List<ProductInspect> inspectRaws) {
        String sql = """
                INSERT INTO product_workflow_adopt (product_id, inspect_date, create_date, last_modified_date)
                VALUES (:productId, :inspectDate, :createDate, :lastModifiedDate)
                """;

        var params = inspectRaws.stream()
                .map(i -> new MapSqlParameterSource()
                        .addValue("productId", i.productId())
                        .addValue("inspectDate", i.inspectDate())
                        .addValue("createDate", LocalDateTime.now())
                        .addValue("lastModifiedDate", LocalDateTime.now())
                ).toArray(MapSqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(sql, params);
    }

    @Override
    public void materializeJoinAdopt(LocalDateTime fromDt, LocalDateTime toDt) {

    }
}
