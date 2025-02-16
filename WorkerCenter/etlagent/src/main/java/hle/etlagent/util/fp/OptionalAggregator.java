package hle.etlagent.util.fp;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OptionalAggregator<T> {
    private Boolean isNone = false;
    private final List<T> somes = new ArrayList<>();

    public void markNone() {
        isNone = true;
    }

    public void addSome(T some) {
        somes.add(some);
    }

    public void combine(OptionalAggregator<T> otherAgg) {
        isNone = isNone || otherAgg.isNone;
        somes.addAll(otherAgg.getSomes());
    }
}
