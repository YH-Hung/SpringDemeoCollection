package hle.jpacookbook.repo;

import hle.jpacookbook.model.judge.DefectEdit;
import hle.jpacookbook.model.judge.DefectView;
import hle.jpacookbook.model.query.DefectQuery;
import hle.jpacookbook.model.query.InspectQuery;
import hle.jpacookbook.model.query.JudgeQuery;
import io.vavr.control.Option;

import java.util.List;

public interface DefectAggregateRepository {
    Option<DefectView> getRecordById(DefectQuery defectQuery);
    List<DefectView> getRecordsByInsp(InspectQuery inspectQuery);
    List<DefectView> getRecordsByJudge(JudgeQuery judgeQuery);
    List<DefectEdit> getEditModels(InspectQuery inspectQuery);
}
