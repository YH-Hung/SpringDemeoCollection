package hle.etlagent.util.fp;

import java.util.function.Consumer;
import java.util.function.Function;

public class Either<L, R> {

    private final L leftValue;
    private final R rightValue;
    private final EitherChoice choice;


    private Either(L leftValue, R rightValue, EitherChoice choice) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.choice = choice;
    }

    public static <L, R> Either<L, R> ofRight(R r) {
        return new Either<>(null, r, EitherChoice.RIGHT);
    }

    public static <L, R> Either<L, R> ofLeft(L left) {
        return new Either<>(left, null, EitherChoice.LEFT);
    }

    public <K> Either<L, K> map(Function<? super R, ? extends K> transformer) {
        return switch (choice) {
            case LEFT -> ofLeft(leftValue);
            case RIGHT -> ofRight(transformer.apply(rightValue));
        };
    }

    public Either<L, R> tap(Consumer<? super R> consumer) {
        return switch (choice) {
            case LEFT -> ofLeft(leftValue);
            case RIGHT -> {
                consumer.accept(rightValue);
                yield  ofRight(rightValue);
            }
        };
    }

    public <K> Either<L, K> flatMap(Function<? super R, Either<L, K>> transformer) {
        return switch (choice) {
            case LEFT -> ofLeft(leftValue);
            case RIGHT -> transformer.apply(rightValue);
        };
    }

    public <V> V match(Function<L, V> leftMapper, Function<? super R, V> rightMapper) {
        return switch (choice) {
            case LEFT -> leftMapper.apply(leftValue);
            case RIGHT -> rightMapper.apply(rightValue);
        };
    }

    public void matchConsume(Consumer<? super L> leftConsumer, Consumer<? super R> rightConsumer) {
        switch (choice) {
            case LEFT -> leftConsumer.accept(leftValue);
            case RIGHT -> rightConsumer.accept(rightValue);
        }
    }

    public boolean isLeft() {
        return switch (choice) {
            case LEFT -> true;
            case RIGHT -> false;
        };
    }

    public boolean isRight() {
        return switch (choice) {
            case LEFT -> false;
            case RIGHT -> true;
        };
    }
}
