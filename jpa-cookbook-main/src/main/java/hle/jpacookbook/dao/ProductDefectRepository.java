package hle.jpacookbook.dao;

import hle.jpacookbook.entity.ProductDefect;
import hle.jpacookbook.entity.ProductDefectId;
import io.vavr.control.Option;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductDefectRepository extends JpaRepository<ProductDefect, ProductDefectId>,
        JpaSpecificationExecutor<ProductDefect>,
        QueryByExampleExecutor<ProductDefect>
{
    @EntityGraph(value = "productDefect.attrs") // Refer to a named entity graph
    Option<ProductDefect> findById_ProductIdAndId_InspectDateAndId_DefectId(Integer productId, LocalDateTime inspectDate, Integer defectId);

    @EntityGraph(attributePaths = {"attrs"})    // Create an inline entity graph
    List<ProductDefect> findById_ProductIdAndId_InspectDate(Integer productId, LocalDateTime inspectDate);

    @EntityGraph(value = "productDefect.attrs") // Refer to a named entity graph
    List<ProductDefect> findByAttrs_TypeAndAttrs_Val(String type, String val);

    @Override
    @EntityGraph(value = "productDefect.attrs")
    @NotNull
    List<ProductDefect> findAll(@NotNull Specification<ProductDefect> spec);

}