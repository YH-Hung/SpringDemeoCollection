package hle.etlagent.dao.jpa.inline;

import hle.etlagent.entity.inline.ProductInspect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductInspectRepository extends JpaRepository<ProductInspect, Integer> {
    List<ProductInspect> findAllByInspectDateBetween(LocalDateTime inspectDateAfter, LocalDateTime inspectDateBefore);
}