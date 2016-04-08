package functionality;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Alexey on 19.03.2016.
 */
public class PredicateTest {

    private static final Predicate<Integer> EVEN_NUMBER = arg -> arg % 2 == 0;
    private static final Predicate<Integer> DEVIDED_ON_5 = arg -> arg % 5 == 0;
    private static final Predicate<Object> FAILED_PREDICATE = arg -> {
        throw new IllegalStateException();
    };

    @Test
    public void testPredicate() throws Exception {
        assertTrue(EVEN_NUMBER.apply(0));
        assertTrue(EVEN_NUMBER.apply(4));
        assertFalse(EVEN_NUMBER.apply(5));
        assertFalse(EVEN_NUMBER.apply(-1));
        assertTrue(EVEN_NUMBER.apply(-2));
    }

    @Test
    public void testOr() throws Exception {
        Predicate<Integer> pDiv2Or10 = EVEN_NUMBER.or(DEVIDED_ON_5);
        assertTrue(pDiv2Or10.apply(0));
        assertFalse(pDiv2Or10.apply(1));
        assertTrue(pDiv2Or10.apply(2));
        assertTrue(pDiv2Or10.apply(5));
        assertFalse(pDiv2Or10.apply(7));
        assertFalse(pDiv2Or10.apply(-23));
    }

    @Test
    public void testAnd() throws Exception {
        Predicate<Integer> pDiv10 = EVEN_NUMBER.and(DEVIDED_ON_5);
        assertTrue(pDiv10.apply(0));
        assertFalse(pDiv10.apply(23));
        assertTrue(pDiv10.apply(10));
        assertFalse(pDiv10.apply(5));
        assertFalse(pDiv10.apply(-15));
        assertFalse(pDiv10.apply(3));
    }

    @Test
    public void testNot() throws Exception {
        Predicate<Integer> pOdd = EVEN_NUMBER.not();
        assertFalse(pOdd.apply(0));
        assertFalse(pOdd.apply(4));
        assertTrue(pOdd.apply(5));
        assertTrue(pOdd.apply(-1));
        assertFalse(pOdd.apply(-2));
    }

    @Test
    public void testAlwaysTrue() throws Exception {
        assertTrue(Predicate.ALWAYS_TRUE.apply(0));
        assertTrue(Predicate.ALWAYS_TRUE.apply("false"));
        assertTrue(Predicate.ALWAYS_TRUE.apply(false));
    }

    @Test
    public void testAlwaysFalse() throws Exception {
        assertFalse(Predicate.ALWAYS_FALSE.apply(0));
        assertFalse(Predicate.ALWAYS_FALSE.apply("false"));
        assertFalse(Predicate.ALWAYS_FALSE.apply(false));
        assertFalse(Predicate.ALWAYS_FALSE.and(Predicate.ALWAYS_TRUE).apply("C#"));
        assertTrue(Predicate.ALWAYS_FALSE.or(Predicate.ALWAYS_TRUE).apply("LINQ"));
    }

    @Test
    public void testLazy() throws Exception {
        assertTrue(Predicate.ALWAYS_TRUE.or(FAILED_PREDICATE).apply(0));
        assertFalse(Predicate.ALWAYS_FALSE.and(FAILED_PREDICATE).apply(0));
    }

    @Test
    public void testWildcard() throws Exception {
        Predicate<Number> pMoreThenFive = arg -> arg.intValue() > 5;
        assertFalse(pMoreThenFive.apply(5));
        assertTrue(pMoreThenFive.apply(10.328763));
    }

}