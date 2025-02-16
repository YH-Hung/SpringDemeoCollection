package hle.etlagent.util.fp;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class EitherSequenceCollector<Left, Right> implements Collector<Either<Left, Right>, EitherAggregator<Left, Right>, Either<Left, List<Right>>> {

    @Override
    public Supplier<EitherAggregator<Left, Right>> supplier() {
        return EitherAggregator::new;
    }

    @Override
    public BiConsumer<EitherAggregator<Left, Right>, Either<Left, Right>> accumulator() {
        return (agg, elem) -> elem.matchConsume(agg::addLeft, agg::addRight);
    }

    @Override
    public BinaryOperator<EitherAggregator<Left, Right>> combiner() {
        return (agg1, agg2) -> {
            agg1.combine(agg2);
            return agg1;
        };
    }

    @Override
    public Function<EitherAggregator<Left, Right>, Either<Left, List<Right>>> finisher() {
        return agg -> {
            if (agg.getLefts().isEmpty()) {
                return Either.ofRight(agg.getRights());
            } else {
                return Either.ofLeft(agg.getLefts().get(0));
            }
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.CONCURRENT);
    }

    public static <Left, Right> EitherSequenceCollector<Left, Right> toEitherSequence() {
        return new EitherSequenceCollector<>();
    }
}
