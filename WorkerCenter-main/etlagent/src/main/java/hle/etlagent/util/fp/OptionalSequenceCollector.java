package hle.etlagent.util.fp;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class OptionalSequenceCollector<T> implements Collector<Optional<T>, OptionalAggregator<T>, Optional<List<T>>> {
    @Override
    public Supplier<OptionalAggregator<T>> supplier() {
        return OptionalAggregator::new;
    }

    @Override
    public BiConsumer<OptionalAggregator<T>, Optional<T>> accumulator() {
        return (agg, elem) -> elem.ifPresentOrElse(agg::addSome, agg::markNone);
    }

    @Override
    public BinaryOperator<OptionalAggregator<T>> combiner() {
        return (agg1, agg2) -> {
            agg1.combine(agg2);
            return agg1;
        };
    }

    @Override
    public Function<OptionalAggregator<T>, Optional<List<T>>> finisher() {
        return agg -> {
            if (agg.getIsNone()) return Optional.empty();
            else return Optional.of(agg.getSomes());
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.CONCURRENT);
    }

    public static <T> OptionalSequenceCollector<T> tOptionalSequence() {
        return new OptionalSequenceCollector<>();
    }
}
