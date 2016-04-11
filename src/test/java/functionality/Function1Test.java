package functionality;

import static org.junit.Assert.assertEquals;

/**
 * Created by Alexey on 18.03.2016.
 */
public class Function1Test {


    @org.junit.Test
    public void testApply() throws Exception {
        FunPlus5 p5 = new FunPlus5();
        assertEquals(5, (int) p5.apply(0));
        FunMult10 m10 = new FunMult10();
        assertEquals(230, (int) m10.apply(23));
    }

    @org.junit.Test
    public void testCompose() throws Exception {
        FunPlus5 p5 = new FunPlus5();
        FunMult10 m10 = new FunMult10();
        Function1<Number, Integer> m7 = arg -> arg.intValue() * 7;
        Function1<Integer, String> tStr = arg -> "<" + arg.toString() + ">";

        Function1<Integer, Integer> p5m10 = p5.compose(m10);
        assertEquals(50, (int) p5m10.apply(0));

        Function1<Integer, Integer> m10p5 = m10.compose(p5);
        assertEquals(55, (int) m10p5.apply(5));

        assertEquals("<35>", m7.compose(tStr).apply(5));
    }

    private static class FunPlus5 implements Function1<Integer, Integer> {
        @Override
        public Integer apply(Integer arg) {
            return arg + 5;
        }
    }

    private static class FunMult10 implements Function1<Integer, Integer> {
        @Override
        public Integer apply(Integer arg) {
            return arg * 10;
        }
    }

}