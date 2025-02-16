package hle.etlagent.util.fp;

import java.util.function.Function;

public class Pipable<T> {
    private final T payload;


    private Pipable(T payload) {
        this.payload = payload;
    }

    public Pipable<T> of(T payload) {
        return new Pipable<>(payload);
    }

    public <K> Pipable<K> pipe(Function<T, K> transformer) {
        return new Pipable<>(transformer.apply(payload));
    }

    public T get() {
        return payload;
    }
}
