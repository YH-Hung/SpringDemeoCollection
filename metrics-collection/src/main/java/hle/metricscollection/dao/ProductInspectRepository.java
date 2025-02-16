package hle.metricscollection.dao;

import hle.metricscollection.entity.ProductInspect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInspectRepository extends JpaRepository<ProductInspect, Integer> {
}