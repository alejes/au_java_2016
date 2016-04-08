package functionality;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Alexey on 18.03.2016.
 */
public class Function2Test {

    @Test
    public void testApply() throws Exception {
        Sum plus = new Sum();
        assertEquals(23, (int) plus.apply(10, 13));
    }


    @Test
    public void testCompose() throws Exception {
        FunMult10 m10 = new FunMult10();
        Sum plus = new Sum();

        Function2<Integer, Integer, Integer> plustm10 = plus.compose(m10);
        assertEquals(230, (int) plustm10.apply(10, 13));
    }

    @Test
    public void testBind1() throws Exception {
        Sum plus = new Sum();
        Function1<Integer, Integer> plus5 = plus.bind1(5);
        assertEquals(54, (int) plus5.apply(49));

        Div div = new Div();
        Function1<Integer, Double> divFrom100 = div.bind1(100);
        assertEquals(20, divFrom100.apply(5), 1E-6);
    }

    @Test
    public void testBind2() throws Exception {
        Div div = new Div();
        Function1<Integer, Double> divOn2 = div.bind2(2);
        assertEquals(54, divOn2.apply(108), 1E-6);
    }

    @Test
    public void testCurry() throws Exception {
        Sum plus = new Sum();
        Function1<Integer, Function1<Integer, Integer>> plus1Arg = plus.curry();
        Function1 plus21 = plus1Arg.apply(21);
        assertEquals(50, plus21.apply(29));

    }

    private static class FunMult10 implements Function1<Integer, Integer> {
        @Override
        public Integer apply(Integer arg) {
            return arg * 10;
        }
    }

    private static class Sum implements Function2<Integer, Integer, Integer> {
        @Override
        public Integer apply(Integer arg1, Integer arg2) {
            return arg1 + arg2;
        }
    }

    private static class Div implements Function2<Integer, Integer, Double> {
        @Override
        public Double apply(Integer arg1, Integer arg2) {
            return arg1.doubleValue() / arg2.doubleValue();
        }
    }

}