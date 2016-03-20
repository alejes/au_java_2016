package functionality;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


public class CollectionsTest {


    class FunPlus5 implements  Function1<Integer, Integer>{
        @Override
        public Integer apply(Integer arg) {
            return arg + 5;
        }
    }
    class EvenNumber implements Predicate<Integer>{
        @Override
        public Boolean apply(Integer arg) {
            return arg % 2 == 0;
        }
    }
    @Test
    public void testMap() throws Exception {
        List<Integer> inputList = Arrays.asList(1,2,3,4,5,6);
        List<Integer> resultList = Arrays.asList(6,7,8,9,10, 11);

        assertTrue(resultList.equals(Collections.map(new FunPlus5(), inputList)));
    }

    @Test
    public void testFilter() throws Exception {
        List<Integer> inputList = Arrays.asList(1,2,3,4,5,6);
        List<Integer> resultList = Arrays.asList(2,4,6);

        assertTrue(resultList.equals(Collections.filter(new EvenNumber(), inputList)));
    }

    @Test
    public void testTakeWhile() throws Exception {
        List<Integer> inputList = Arrays.asList(2,4,6,8,10,11,12,14,23,96);
        List<Integer> resultList = Arrays.asList(2,4,6,8,10);

        assertTrue(resultList.equals(Collections.takeWhile(new EvenNumber(), inputList)));
    }

    @Test
    public void testTakeUnless() throws Exception {
        List<Integer> inputList = Arrays.asList(1,3,5,7,9,11,24,29,1092,3323);
        List<Integer> resultList = Arrays.asList(1,3,5,7,9,11);

        assertTrue(resultList.equals(Collections.takeUnless(new EvenNumber(), inputList)));
    }

    class Sum implements  Function2<Integer, Integer, Integer>{
        @Override
        public Integer apply(Integer arg1, Integer arg2) {
            return arg1 + arg2;
        }
    }

    class Sub implements  Function2<Integer, Integer, Integer>{
        @Override
        public Integer apply(Integer arg1, Integer arg2) {
            return arg1 - arg2;
        }
    }

    class Concat implements  Function2<String, String, String>{
        @Override
        public String apply(String arg1, String arg2) {
            return arg1 + arg2;
        }
    }

    @Test
    public void testFoldl() throws Exception {
        List<Integer> inputList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);

        assertEquals(55, (int)Collections.foldl(new Sum(), 0, inputList));

        List<String> inputStrList = Arrays.asList("M", "A", "M", "A");
        assertEquals("MAMA", (String)Collections.foldl(new Concat(), "", inputStrList));

        List<Integer> inputSubList = Arrays.asList(1,2,3,4,5);

        assertEquals(85, (int)Collections.foldl(new Sub(), 100, inputSubList));
    }

    @Test
    public void testFoldr() throws Exception {
        List<Integer> inputList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);

        assertEquals(55, (int)Collections.foldr(new Sum(), 0, inputList));

        List<String> inputStrList = Arrays.asList("M", "A", "M", "A");
        assertEquals("MAMA", (String)Collections.foldr(new Concat(), "", inputStrList));

        Integer inputSub[] = {1,2,3,4,5};
        List<Integer> inputSubList = Arrays.asList(inputSub);

        assertEquals(-97, (int)Collections.foldr(new Sub(), 100, inputSubList));
    }
}