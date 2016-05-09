package ru.spbau.stepanov;

import java.util.function.Function;

/**
 * Created by Alexey on 02.05.2016.
 */
public interface LightFuture<V> {
    boolean isReady();

    V get() throws LightExecutionException;

    <R> LightFuture<R> thenApply(Function<? super V, ? extends R> f);
}
