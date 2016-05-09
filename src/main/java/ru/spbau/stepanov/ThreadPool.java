package ru.spbau.stepanov;

import java.util.function.Supplier;

/**
 * Created by Alexey on 02.05.2016.
 */
public interface ThreadPool {
    <T> LightFuture pushTask(Supplier<T> suppl);

    void shutdown();
}
