package hle.jpacookbook.mapper;

import hle.jpacookbook.constant.DefectAttrType;
import hle.jpacookbook.entity.ProductDefect;
import hle.jpacookbook.entity.ProductDefectAttr;
import hle.jpacookbook.model.judge.DefectEdit;
import hle.jpacookbook.model.judge.DefectView;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.HashMap;
import java.util.Map;

public class DefectMapper {

    public static DefectView entityToView(ProductDefect entity) {
        var view = new DefectView();
        view.setProductId(entity.getId().getProductId());
        view.setInspectDate(entity.getId().getInspectDate());
        view.setDefectId(entity.getId().getDefectId());

        entity.getAttrs().forEach(attr -> Try.of(() -> DefectAttrType.valueOf(attr.getType()))
                .peek(attrType -> {
                    switch (attrType) {
                        case MANUAL_JUDGE_1ST -> view.setManualJudgeCode1st(attr.getVal());
                        case MANUAL_JUDGE_2ND -> view.setManualJudgeCode2nd(attr.getVal());
                        case MANUAL_JUDGE_3RD -> view.setManualJudgeCode3rd(attr.getVal());
                    }
                }));

        return view;
    }

    public static DefectEdit entityToEdit(ProductDefect entity) {
        Map<Integer, ProductDefectAttr> attrMap = new HashMap<>();
        entity.getAttrs().forEach(attr -> Try.of(() -> DefectAttrType.valueOf(attr.getType()))
                .peek(attrType -> {
                    switch (attrType) {
                        case MANUAL_JUDGE_1ST -> attrMap.put(1, attr);
                        case MANUAL_JUDGE_2ND -> attrMap.put(2, attr);
                        case MANUAL_JUDGE_3RD -> attrMap.put(3, attr);
                    }
                }));

        return new DefectEdit(
                entity.getId().getProductId(),
                entity.getId().getInspectDate(),
                entity.getId().getDefectId(),
                Option.of(attrMap.getOrDefault(1, null)),
                Option.of(attrMap.getOrDefault(2, null)),
                Option.of(attrMap.getOrDefault(3, null))
        );
    }
}
