package hle.etlagent.dao.impl.rowmapper;

import hle.etlagent.model.ProductInspect;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ProductInspectRowMapper implements RowMapper<ProductInspect> {
    @Override
    public ProductInspect mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ProductInspect(
                rs.getInt("uid"),
                rs.getString("product_id"),
                rs.getObject("inspect_date", LocalDateTime.class),
                rs.getObject("create_date", LocalDateTime.class),
                rs.getObject("last_modified_date", LocalDateTime.class)
                );
    }
}
