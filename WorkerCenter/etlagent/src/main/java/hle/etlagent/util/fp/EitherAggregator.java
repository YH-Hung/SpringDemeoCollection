package hle.etlagent.util.fp;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EitherAggregator<Left, Right> {
    private final List<Left> lefts = new ArrayList<>();
    private final List<Right> rights = new ArrayList<>();

    public void addLeft(Left left) {
        lefts.add(left);
    }

    public void addRight(Right right) {
        rights.add(right);
    }

    public void combine(EitherAggregator<Left, Right> otherAgg) {
        lefts.addAll(otherAgg.getLefts());
        rights.addAll(otherAgg.getRights());
    }
}
