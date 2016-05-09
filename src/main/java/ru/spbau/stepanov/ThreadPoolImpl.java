package ru.spbau.stepanov;

import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * Created by Alexey on 02.05.2016.
 */
public class ThreadPoolImpl implements ThreadPool {
    private final Thread[] threads;
    private final LinkedList<LightFutureImpl> tasks = new LinkedList<>();


    public ThreadPoolImpl(int n) {
        threads = new Thread[n];
        for (int i = 0; i < n; ++i) {
            threads[i] = getThread();
            threads[i].start();
        }
    }

    public <T> LightFuture pushTask(Supplier<T> suppl) {
        LightFutureImpl task = new LightFutureImpl<T>(suppl);
        synchronized (tasks) {
            tasks.add(task);
            tasks.notify();
        }
        return task;
    }

    @Override
    public void shutdown() {
        LightExecutionException exc = new LightExecutionException();
        synchronized (tasks) {
            for (LightFutureImpl lf : tasks) {
                lf.cancelAll(exc);
            }
        }
        for (Thread t : threads) {
            t.interrupt();
        }
    }

    private Thread getThread() {
        return new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    LightFutureImpl myTask = null;
                    synchronized (tasks) {
                        while (tasks.isEmpty()) {
                            tasks.wait();
                        }
                        myTask = tasks.removeLast();
                    }
                    myTask.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    public class LightFutureImpl<T> implements LightFuture<T> {
        private final LinkedList<LightFutureImpl> thenApplyList = new LinkedList<>();
        private final Supplier<T> supplier;
        private volatile LightExecutionException futureException = null;
        private volatile boolean ready = false;
        private volatile T result;

        public LightFutureImpl(Supplier<T> supp) {
            supplier = supp;
        }

        @Override
        public boolean isReady() {
            return ready;
        }

        @Override
        public T get() throws LightExecutionException {
            while (!isReady()) {
                synchronized (this) {
                    while (!isReady()) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            throw new LightExecutionException(e);
                        }
                    }
                }
            }

            if (futureException != null) {
                throw futureException;
            }

            return result;
        }


        @Override
        public <R> LightFuture<R> thenApply(Function<? super T, ? extends R> f) {
            Supplier<R> suppl = () -> {
                try {
                    return f.apply(get());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            LightFutureImpl<R> applyTask = null;
            if (futureException != null) {
                applyTask = new LightFutureImpl<>(suppl);
                applyTask.futureException = futureException;
                applyTask.ready = true;
            } else {
                if (isReady()) {
                    applyTask = (LightFutureImpl<R>) pushTask(suppl);
                } else {
                    synchronized (thenApplyList) {
                        applyTask = new LightFutureImpl<>(suppl);
                        thenApplyList.add(applyTask);
                    }
                }
            }
            return applyTask;
        }

        private void releaseThenApply() {
            synchronized (tasks) {
                synchronized (thenApplyList) {
                    for (LightFutureImpl tsk : thenApplyList) {
                        tasks.add(tsk);
                    }
                    tasks.notifyAll();
                    thenApplyList.clear();
                }
            }
        }

        private void run() {
            try {
                result = supplier.get();
                releaseThenApply();
                ready = true;
            } catch (Exception exp) {
                futureException = new LightExecutionException(exp);
                cancel();
            }
            synchronized (this) {
                notifyAll();
            }
        }

        private void cancel() {
            cancelAll(futureException);
        }

        private void cancelAll(LightExecutionException exception) {
            futureException = exception;
            synchronized (thenApplyList) {
                for (LightFutureImpl light : thenApplyList) {
                    light.cancelAll(exception);
                }
                thenApplyList.clear();
            }
            ready = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }
}
