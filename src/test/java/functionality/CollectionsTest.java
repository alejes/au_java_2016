package functionality;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class CollectionsTest {

    @Test
    public void testMap() throws Exception {
        List<Integer> inputList = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> resultList = Arrays.asList(6, 7, 8, 9, 10, 11);

        assertEquals(resultList, Collections.map(arg -> arg + 5, inputList));
    }

    @Test
    public void testFilter() throws Exception {
        List<Integer> inputList = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> resultList = Arrays.asList(2, 4, 6);

        assertEquals(resultList, Collections.filter(arg -> arg % 2 == 0, inputList));
    }

    @Test
    public void testTakeWhile() throws Exception {
        List<Integer> inputList = Arrays.asList(2, 4, 6, 8, 10, 11, 12, 14, 23, 96);
        List<Integer> resultList = Arrays.asList(2, 4, 6, 8, 10);

        assertEquals(resultList, Collections.takeWhile(arg -> arg % 2 == 0, inputList));
    }

    @Test
    public void testTakeUnless() throws Exception {
        List<Integer> inputList = Arrays.asList(1, 3, 5, 7, 9, 11, 24, 29, 1092, 3323);
        List<Integer> resultList = Arrays.asList(1, 3, 5, 7, 9, 11);

        assertEquals(resultList, Collections.takeUnless(arg -> arg % 2 == 0, inputList));
    }

    @Test
    public void testFoldl() throws Exception {
        List<Integer> inputList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        assertEquals(55, (int) Collections.foldl((arg1, arg2) -> arg1 + arg2, 0, inputList));

        List<String> inputStrList = Arrays.asList("M", "A", "M", "A");
        assertEquals("MAMA", Collections.foldl((arg1, arg2) -> arg1 + arg2, "", inputStrList));

        List<Integer> inputSubList = Arrays.asList(1, 2, 3, 4, 5);

        assertEquals(85, (int) Collections.foldl((arg1, arg2) -> arg1 - arg2, 100, inputSubList));
    }

    @Test
    public void testFoldr() throws Exception {
        List<Integer> inputList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        assertEquals(55, (int) Collections.foldr((arg1, arg2) -> arg1 + arg2, 0, inputList));

        List<String> inputStrList = Arrays.asList("M", "A", "M", "A");
        assertEquals("MAMA", Collections.foldr((arg1, arg2) -> arg1 + arg2, "", inputStrList));

        Integer inputSub[] = {1, 2, 3, 4, 5};
        List<Integer> inputSubList = Arrays.asList(inputSub);

        assertEquals(-97, (int) Collections.foldr((arg1, arg2) -> arg1 - arg2, 100, inputSubList));

    }

    @Test
    public void testFoldWildcard() throws Exception {
        Function2<Number, Integer, Integer> integerFoldrSum = (x, y) -> x.intValue() + y;
        List<Number> inputList = Arrays.asList(1, 2, 3, 4, 5);
        assertEquals((Integer) 15, Collections.foldr(integerFoldrSum, 0, inputList));

        Function2<Integer, Number, Integer> integerFoldlSum = (x, y) -> x + y.intValue();
        assertEquals((Integer) 15, Collections.foldl(integerFoldlSum, 0, inputList));
    }

}