package hle.jpacookbook.repo.impl;

import hle.jpacookbook.dao.ProductDefectRepository;
import hle.jpacookbook.entity.ProductDefect;
import hle.jpacookbook.entity.ProductDefectId;
import hle.jpacookbook.mapper.DefectMapper;
import hle.jpacookbook.model.judge.DefectEdit;
import hle.jpacookbook.model.judge.DefectView;
import hle.jpacookbook.model.query.DefectQuery;
import hle.jpacookbook.model.query.InspectQuery;
import hle.jpacookbook.model.query.JudgeQuery;
import hle.jpacookbook.repo.DefectAggregateRepository;
import io.vavr.control.Option;
import jakarta.persistence.criteria.JoinType;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Primary
@Repository
public class DefectAggregateRepositorySpecImpl implements DefectAggregateRepository {

    public final ProductDefectRepository defectRepository;

    public DefectAggregateRepositorySpecImpl(ProductDefectRepository defectRepository) {
        this.defectRepository = defectRepository;
    }


    @Override
    public Option<DefectView> getRecordById(DefectQuery defectQuery) {
        var id = new ProductDefectId();
        id.setProductId(defectQuery.productId());
        id.setInspectDate(defectQuery.inspDateTime());
        id.setDefectId(defectQuery.defectId());

        Specification<ProductDefect> idSpec = (root, query, builder) ->
                builder.equal(root.get("id"), id);

        return Option.ofOptional(defectRepository.findOne(idSpec))
                .map(DefectMapper::entityToView);
    }

    @Override
    public List<DefectView> getRecordsByInsp(InspectQuery inspectQuery) {
        Specification<ProductDefect> eqProductId = genEqProductIdSpec(inspectQuery);
        Specification<ProductDefect> eqInspDt = genEqInspDtSpec(inspectQuery);

        return defectRepository.findAll(eqProductId.and(eqInspDt))
                .stream().map(DefectMapper::entityToView)
                .toList();
    }

    @Override
    public List<DefectView> getRecordsByJudge(JudgeQuery judgeQuery) {
        Specification<ProductDefect> eqType = (root, query, builder) ->
                builder.equal(root.join("attrs", JoinType.LEFT).get("type"), judgeQuery.type());

        Specification<ProductDefect> eqVal = (root, query, builder) ->
                builder.equal(root.join("attrs", JoinType.LEFT).get("val"), judgeQuery.judgeVal());

        return defectRepository.findAll(eqType.and(eqVal))
                .stream().map(DefectMapper::entityToView)
                .toList();
    }

    @Override
    public List<DefectEdit> getEditModels(InspectQuery inspectQuery) {
        Specification<ProductDefect> eqProductId = genEqProductIdSpec(inspectQuery);
        Specification<ProductDefect> eqInspDt = genEqInspDtSpec(inspectQuery);

        return defectRepository.findAll(eqProductId.and(eqInspDt))
                .stream().map(DefectMapper::entityToEdit)
                .toList();
    }

    private static @NotNull Specification<ProductDefect> genEqInspDtSpec(InspectQuery inspectQuery) {
        return (root, query, builder) ->
                builder.equal(root.get("id").get("inspectDate"), inspectQuery.inspDateTime());
    }

    private static @NotNull Specification<ProductDefect> genEqProductIdSpec(InspectQuery inspectQuery) {
        return (root, query, builder) ->
                builder.equal(root.get("id").get("productId"), inspectQuery.productId());
    }
}
