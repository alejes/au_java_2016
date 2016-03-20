package functionality;

import com.sun.scenario.effect.impl.prism.PrDrawable;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alexey on 19.03.2016.
 */
public class PredicateTest {
    class EvenNumber implements Predicate<Integer>{
        @Override
        public Boolean apply(Integer arg) {
            return arg % 2 == 0;
        }
    }
    class DevidedOn5 implements Predicate<Integer>{
        @Override
        public Boolean apply(Integer arg) {
            return arg % 5 == 0;
        }
    }

    @Test
    public void testPredicate() throws Exception {
        Predicate<Integer> pEven = new EvenNumber();

        assertTrue(pEven.apply(0));
        assertTrue(pEven.apply(4));
        assertFalse(pEven.apply(5));
        assertFalse(pEven.apply(-1));
        assertTrue(pEven.apply(-2));
    }

    @Test
    public void testOr() throws Exception {
        Predicate<Integer> pEven = new EvenNumber();
        Predicate<Integer> pDiv5 = new DevidedOn5();

        Predicate<Integer> pDiv2Or10 = pEven.or(pDiv5);

        assertTrue(pDiv2Or10.apply(0));
        assertFalse(pDiv2Or10.apply(1));
        assertTrue(pDiv2Or10.apply(2));
        assertTrue(pDiv2Or10.apply(5));
        assertFalse(pDiv2Or10.apply(7));
        assertFalse(pDiv2Or10.apply(-23));
    }

    @Test
    public void testAnd() throws Exception {
        Predicate<Integer> pEven = new EvenNumber();
        Predicate<Integer> pDiv5 = new DevidedOn5();

        Predicate<Integer> pDiv10 = pEven.and(pDiv5);

        assertTrue(pDiv10.apply(0));
        assertFalse(pDiv10.apply(23));
        assertTrue(pDiv10.apply(10));
        assertFalse(pDiv10.apply(5));
        assertFalse(pDiv10.apply(-15));
        assertFalse(pDiv10.apply(3));
    }

    @Test
    public void testNot() throws Exception {
        Predicate<Integer> pOdd = new EvenNumber().not();
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
    }
}