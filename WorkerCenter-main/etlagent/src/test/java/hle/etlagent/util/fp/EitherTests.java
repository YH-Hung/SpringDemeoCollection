package hle.etlagent.util.fp;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class EitherTests {

    @Test
    void isLeft() {
        var either = Either.ofLeft(1);
        assertThat(either.isLeft()).isTrue();
    }

    @Test
    void isRight() {
        var either = Either.ofRight(1);
        assertThat(either.isRight()).isTrue();
    }

    @Test
    void leftMapGetLeft() {
        Either<Integer, Integer> either = Either.ofLeft(1);
        var result = either.map(r -> r + 1);
        assertThat(result.match(Function.identity(), Function.identity())).isEqualTo(1);
    }

    @Test
    void rightMapGetRight() {
        Either<Integer, Integer> either = Either.ofRight(1);
        var result = either.map(r -> r + 1);
        assertThat(result.match(Function.identity(), Function.identity())).isEqualTo(2);
    }

    @Test
    void leftFlatMapGetLeft() {
        Function<Integer, Either<Integer, Integer>> mayError = i -> {
            if (i.equals(3)) {
                return Either.ofLeft(3);
            } else {
                return Either.ofRight(i + 1);
            }
        };

        Either<Integer, Integer> either = Either.ofRight(3);
        var result = either.flatMap(mayError);

        assertThat(result.match(Function.identity(), Function.identity())).isEqualTo(3);
    }

    @Test
    void rightFlatMapGetRight() {
        Function<Integer, Either<Integer, Integer>> mayError = i -> {
            if (i.equals(3)) {
                return Either.ofLeft(3);
            } else {
                return Either.ofRight(i + 1);
            }
        };

        Either<Integer, Integer> either = Either.ofRight(4);
        var result = either.flatMap(mayError);

        assertThat(result.match(Function.identity(), Function.identity())).isEqualTo(5);
    }

    @Test
    void sequenceRight() {
        List<Either<Integer, Integer>> eithers = List.of(Either.ofRight(1), Either.ofRight(2), Either.ofRight(3));
        var eitherList = eithers.stream().collect(EitherSequenceCollector.toEitherSequence());
        var result = eitherList.match(Function.identity(), rList -> rList.stream().mapToInt(Integer::intValue).sum());

        assertThat(result).isEqualTo(6);
    }

    @Test
    void sequenceLeft() {
        List<Either<Integer, Integer>> eithers = List.of(Either.ofLeft(1), Either.ofRight(2), Either.ofRight(3));
        var eitherList = eithers.stream().collect(EitherSequenceCollector.toEitherSequence());
        var result = eitherList.match(Function.identity(), rList -> rList.stream().mapToInt(Integer::intValue).sum());

        assertThat(result).isEqualTo(1);
    }
}