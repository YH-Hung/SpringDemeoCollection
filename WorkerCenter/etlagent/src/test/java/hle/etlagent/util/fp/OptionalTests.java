package hle.etlagent.util.fp;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OptionalTests {

    @Test
    void sequenceSome() {
        List<Optional<Integer>> optionals = List.of(Optional.of(1), Optional.of(2), Optional.of(3));
        var optionalList = optionals.stream().collect(OptionalSequenceCollector.tOptionalSequence());
        var result = optionalList.map(sList -> sList.stream().mapToInt(Integer::intValue).sum()).orElse(-1);

        assertThat(result).isEqualTo(6);
    }

    @Test
    void sequenceNone() {
        List<Optional<Integer>> optionals = List.of(Optional.empty(), Optional.of(2), Optional.of(3));
        var optionalList = optionals.stream().collect(OptionalSequenceCollector.tOptionalSequence());
        var result = optionalList.map(sList -> sList.stream().mapToInt(Integer::intValue).sum()).orElse(-1);

        assertThat(result).isEqualTo(-1);
    }
}
