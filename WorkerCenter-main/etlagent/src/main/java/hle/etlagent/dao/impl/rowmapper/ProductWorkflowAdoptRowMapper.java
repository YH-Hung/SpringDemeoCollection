package hle.etlagent.dao.impl.rowmapper;

import hle.etlagent.model.ProductWorkflowAdopt;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ProductWorkflowAdoptRowMapper implements RowMapper<ProductWorkflowAdopt> {
    @Override
    public ProductWorkflowAdopt mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ProductWorkflowAdopt(
                rs.getInt("product_id"),
                rs.getObject("inspect_date", LocalDateTime.class),
                rs.getString("adopt")
                );
    }
}
