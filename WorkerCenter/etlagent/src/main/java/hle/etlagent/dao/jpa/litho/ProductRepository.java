package hle.etlagent.dao.jpa.litho;

import hle.etlagent.entity.litho.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}