package hle.jpacookbook.repo.impl;

import hle.jpacookbook.dao.ProductDefectRepository;
import hle.jpacookbook.mapper.DefectMapper;
import hle.jpacookbook.model.judge.DefectEdit;
import hle.jpacookbook.model.judge.DefectView;
import hle.jpacookbook.model.query.DefectQuery;
import hle.jpacookbook.model.query.InspectQuery;
import hle.jpacookbook.model.query.JudgeQuery;
import hle.jpacookbook.repo.DefectAggregateRepository;
import io.vavr.control.Option;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Primary
@Repository
public class DefectAggregateRepositoryImpl implements DefectAggregateRepository {

    public final ProductDefectRepository defectRepository;

    public DefectAggregateRepositoryImpl(ProductDefectRepository defectRepository) {
        this.defectRepository = defectRepository;
    }

    @Override
    public Option<DefectView> getRecordById(DefectQuery defectQuery) {
        return defectRepository.findById_ProductIdAndId_InspectDateAndId_DefectId(defectQuery.productId(), defectQuery.inspDateTime(), defectQuery.defectId())
                .map(DefectMapper::entityToView);
    }

    @Override
    public List<DefectView> getRecordsByInsp(InspectQuery inspectQuery) {
        return defectRepository.findById_ProductIdAndId_InspectDate(inspectQuery.productId(), inspectQuery.inspDateTime())
                .stream().map(DefectMapper::entityToView)
                .toList();
    }

    @Override
    public List<DefectView> getRecordsByJudge(JudgeQuery judgeQuery) {
        return defectRepository.findByAttrs_TypeAndAttrs_Val(judgeQuery.type(), judgeQuery.judgeVal())
                .stream().map(DefectMapper::entityToView)
                .toList();
    }

    @Override
    public List<DefectEdit> getEditModels(InspectQuery inspectQuery) {
        return defectRepository.findById_ProductIdAndId_InspectDate(inspectQuery.productId(), inspectQuery.inspDateTime())
                .stream().map(DefectMapper::entityToEdit)
                .toList();
    }

}
