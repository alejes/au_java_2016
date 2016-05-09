package ru.spbau.stepanov;

import org.junit.Test;

import java.util.HashSet;
import java.util.Random;

import static org.junit.Assert.*;

public class ThreadPoolImplTest {
    private static Random random = new Random();
    public final int COUNT_MULTI_THEN_APPLY_TEST = 10;
    public final int MAX_THREAD_TEST = 50;

    private static void waitExecuteFix(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
        }
    }

    private static void waitExecutePause(long pause) {
        waitExecuteFix(1000 + pause);
    }

    private static void waitExecute() {
        waitExecutePause(random.nextInt(1000));
    }

    @Test
    public void simpleTest() throws LightExecutionException {
        ThreadPool tp = new ThreadPoolImpl(1);
        LightFuture lf = tp.pushTask(() -> {
            waitExecute();
            return 53;
        });
        assertFalse(lf.isReady());
        assertEquals(53, (int) lf.get());
        assertTrue(lf.isReady());
    }

    @Test
    public void simpleExceptionTest() {
        ThreadPool tp = new ThreadPoolImpl(1);
        LightFuture lf = tp.pushTask(() -> {
            waitExecute();
            throw new IllegalArgumentException();
        });
        assertFalse(lf.isReady());
        try {
            Object res = lf.get();
        } catch (LightExecutionException e) {
            assertTrue(lf.isReady());
            assertEquals(IllegalArgumentException.class, e.getCause().getClass());
        }
    }

    @Test
    public void simpleThenApplyTest() throws LightExecutionException {
        ThreadPool tp = new ThreadPoolImpl(1);
        LightFuture<Integer> lf = tp.pushTask(() -> {
            waitExecute();
            return 5;
        });
        LightFuture<Object> nf = lf.thenApply((Number r) -> {
            waitExecute();
            return r.intValue() + 5;
        });
        assertFalse(lf.isReady());
        assertFalse(nf.isReady());

        assertEquals(5, (int) lf.get());
        assertTrue(lf.isReady());
        assertFalse(nf.isReady());

        assertEquals(10, nf.get());
        assertTrue(nf.isReady());
    }

    @Test
    public void multiThenApplyTest() throws LightExecutionException {
        ThreadPool tp = new ThreadPoolImpl(5);
        LightFuture<Integer> lf = tp.pushTask(() -> {
            waitExecute();
            return 5;
        });
        LightFuture<Integer> nf[] = new LightFuture[COUNT_MULTI_THEN_APPLY_TEST];

        for (int i = 0; i < COUNT_MULTI_THEN_APPLY_TEST; ++i) {
            final int intValue = i;
            nf[i] = (lf.thenApply((Number r) -> {
                waitExecute();
                return r.intValue() + intValue;
            }));
        }

        assertFalse(lf.isReady());
        for (int i = 0; i < COUNT_MULTI_THEN_APPLY_TEST; ++i) {
            assertFalse(nf[i].isReady());
        }

        assertEquals(5, (int) lf.get());
        assertTrue(lf.isReady());
        for (int i = 0; i < COUNT_MULTI_THEN_APPLY_TEST; ++i) {
            assertFalse(nf[i].isReady());
        }

        for (int i = 0; i < COUNT_MULTI_THEN_APPLY_TEST; ++i) {
            assertEquals((Number) (5 + i), nf[i].get());
        }
        for (int i = 0; i < COUNT_MULTI_THEN_APPLY_TEST; ++i) {
            assertTrue(nf[i].isReady());
        }
    }

    @Test
    public void addThenApplyAfterGetTest() throws LightExecutionException {
        ThreadPool tp = new ThreadPoolImpl(5);
        LightFuture<Integer> lf = tp.pushTask(() -> {
            waitExecute();
            return 5;
        });

        assertFalse(lf.isReady());

        assertEquals(5, (int) lf.get());
        assertTrue(lf.isReady());

        LightFuture<Number> nf = lf.thenApply((Number r) -> {
            waitExecute();
            return r.intValue() + 5;
        });

        assertFalse(nf.isReady());
        assertEquals(10, (int) nf.get());
        assertTrue(nf.isReady());
    }

    @Test(expected = LightExecutionException.class)
    public void exceptionThenApplyAfterGetTest() throws LightExecutionException {
        ThreadPool tp = new ThreadPoolImpl(5);
        LightFuture<Integer> lf = tp.pushTask(() -> {
            waitExecute();
            throw new IndexOutOfBoundsException();
        });

        assertFalse(lf.isReady());

        boolean wasException = false;
        try {
            lf.get();
        } catch (LightExecutionException e) {
            wasException = true;
        }
        assertTrue(wasException);
        assertTrue(lf.isReady());

        LightFuture<Number> nf = lf.thenApply((Number r) -> {
            waitExecute();
            return r.intValue() + 5;
        });
        nf.get();
    }

    @Test(expected = LightExecutionException.class)
    public void exceptionThenApplyWithoutGet() throws LightExecutionException {
        ThreadPool tp = new ThreadPoolImpl(5);
        LightFuture<Integer> lf = tp.pushTask(() -> {
            waitExecute();
            throw new IndexOutOfBoundsException();
        });

        assertFalse(lf.isReady());

        LightFuture<Number> nf = lf.thenApply((Number r) -> {
            waitExecute();
            return r.intValue() + 5;
        });
        nf.get();
    }

    @Test(expected = LightExecutionException.class)
    public void exceptionShutdown() throws LightExecutionException {
        final int THREAD_COUNT = 12;
        final int TASKS_COUNT = 123;
        ThreadPoolImpl tp = new ThreadPoolImpl(THREAD_COUNT);

        LightFuture<Integer> lf[] = new LightFuture[TASKS_COUNT];

        for (int i = 0; i < TASKS_COUNT; ++i) {
            final int intValue = i;
            lf[i] = tp.pushTask(() -> {
                waitExecute();
                return intValue;
            });
        }

        tp.shutdown();

        for (int i = 0; i < TASKS_COUNT; ++i) {
            lf[i].get();
        }
    }


    @Test
    public void countingUniqueThreads() throws LightExecutionException {
        HashSet<Long> hs = new HashSet<>();
        ThreadPool tp = new ThreadPoolImpl(MAX_THREAD_TEST);
        LightFuture<Integer> lf[] = new LightFuture[MAX_THREAD_TEST];
        for (int i = 0; i < MAX_THREAD_TEST; ++i) {
            final int intValue = i;
            lf[i] = tp.pushTask(() -> {
                waitExecutePause(3000);
                hs.add(Thread.currentThread().getId());
                return intValue;
            });
        }

        for (int i = 0; i < MAX_THREAD_TEST; ++i) {
            assertEquals(i, (int) lf[i].get());
        }

        assertEquals(MAX_THREAD_TEST, hs.size());
    }
}