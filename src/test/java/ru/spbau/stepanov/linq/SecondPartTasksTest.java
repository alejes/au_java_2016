package ru.spbau.stepanov.linq;

import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static ru.spbau.stepanov.linq.SecondPartTasks.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() throws IOException {
        List<String> files = IntStream.range(1, 4).mapToObj(s -> "src/test/resources/findQuotes" + s + ".txt").collect(Collectors.toList());
        List<String> result = Arrays.asList("Изысканный бродит жираф.", "И жираф -", "Где трава укрывает жирафа,");

        assertEquals(result, findQuotes(files, "жираф"));
        assertEquals(Collections.EMPTY_LIST, findQuotes(Collections.emptyList(), "жираф"));
    }


    @Test(expected = RuntimeException.class)
    public void testFindQuotesException() throws IOException {
        assertEquals(Collections.EMPTY_LIST, findQuotes(Collections.singletonList("unknownFile.txt"), "жираф"));
    }

    @Test
    public void testPiDividedBy4() {
        assertEquals(Math.PI / 4, piDividedBy4(), 0.01);
    }

    @Test
    public void testFindPrinter() {
        List<String> au1 = Arrays.asList("111", "222", "7");
        List<String> au2 = Arrays.asList("999", "888", "66");
        List<String> au3 = Arrays.asList("333", "444", "555");
        Map<String, List<String>> map = new HashMap<>();
        map.put("Иванов", au1);
        map.put("Горворов", au2);
        map.put("Птицеворов", au3);

        assertEquals("Птицеворов", findPrinter(map));
        assertEquals(null, findPrinter(Collections.emptyMap()));
    }


    @Test
    public void testCalculateGlobalOrder() {
        Map<String, Integer> tv1 = new HashMap<>(), tv2 = new HashMap<>(), tv3 = new HashMap<>(), res = new HashMap<>();
        tv1.put("Orange", 3);
        tv1.put("Apple", 2);

        tv2.put("Apple", 10);
        tv2.put("Juice", 8);

        tv3.put("Chocolate", 86);
        tv3.put("Apple", 123);
        tv3.put("Orange", 1);

        res.put("Chocolate", 86);
        res.put("Apple", 135);
        res.put("Juice", 8);
        res.put("Orange", 4);

        assertEquals(res, calculateGlobalOrder(Arrays.asList(tv1, tv2, tv3)));
        assertEquals(Collections.emptyMap(), calculateGlobalOrder(Collections.emptyList()));
    }
}