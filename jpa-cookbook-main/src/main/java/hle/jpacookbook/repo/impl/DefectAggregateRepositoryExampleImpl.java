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
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;

import java.util.List;

@Primary
@Repository
public class DefectAggregateRepositoryExampleImpl implements DefectAggregateRepository {
    public final ProductDefectRepository defectRepository;

    public DefectAggregateRepositoryExampleImpl(ProductDefectRepository defectRepository) {
        this.defectRepository = defectRepository;
    }


    @Override
    public Option<DefectView> getRecordById(DefectQuery defectQuery) {
        var id = new ProductDefectId();
        id.setProductId(defectQuery.productId());
        id.setInspectDate(defectQuery.inspDateTime());
        id.setDefectId(defectQuery.defectId());

        var entity = new ProductDefect();
        entity.setId(id);

        Example<ProductDefect> example = Example.of(entity);

        return Option.ofOptional(defectRepository.findOne(example))
                .map(DefectMapper::entityToView);
    }

    @Override
    public List<DefectView> getRecordsByInsp(InspectQuery inspectQuery) {
        var id = new ProductDefectId();
        id.setProductId(inspectQuery.productId());
        id.setInspectDate(inspectQuery.inspDateTime());

        var entity = new ProductDefect();
        entity.setId(id);
        Example<ProductDefect> example = Example.of(entity);

        return defectRepository.findAll(example)
                .stream().map(DefectMapper::entityToView)
                .toList();
    }

    @Override
    public List<DefectView> getRecordsByJudge(JudgeQuery judgeQuery) {
        return defectRepository.findAll().stream()
                .filter(d -> d.getAttrs().stream()
                        .anyMatch(a ->
                                a.getType().equals(judgeQuery.type())
                                        && a.getVal().equals(judgeQuery.judgeVal()))
                )
                .map(DefectMapper::entityToView)
                .toList();
    }

    @Override
    public List<DefectEdit> getEditModels(InspectQuery inspectQuery) {
        var id = new ProductDefectId();
        id.setProductId(inspectQuery.productId());
        id.setInspectDate(inspectQuery.inspDateTime());

        var entity = new ProductDefect();
        entity.setId(id);
        Example<ProductDefect> example = Example.of(entity);
        return defectRepository.findAll(example)
                .stream().map(DefectMapper::entityToEdit)
                .toList();
    }
}
