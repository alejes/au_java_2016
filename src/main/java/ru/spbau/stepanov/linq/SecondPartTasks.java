package ru.spbau.stepanov.linq;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private static final int COUNT_TESTS = 1000000;
    private static final Random RANDOM = new Random();

    private SecondPartTasks() {
    }

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) throws IOException {
        // ввиду того что каких-то фиксированных требований небыло, я считаю что тут имеются ввиду различные цитаты.
        // это также проверяется  в тестах, ввиду повторно втсречающейся строки "Изысканный бродит жираф."
        return paths.stream().flatMap(x -> {
            try {
                return Files.lines(Paths.get(x));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).filter(x -> x.contains(sequence)).distinct()
                .collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        return Stream.generate(
                () -> Math.sqrt(Math.pow(RANDOM.nextDouble() - 0.5, 2) + Math.pow(RANDOM.nextDouble() - 0.5, 2)))
                .limit(COUNT_TESTS).filter(u -> u <= 0.5).count() * 1.0 / COUNT_TESTS;


    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet().stream().
                collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                stringListEntry -> stringListEntry.getValue().stream().collect(Collectors.joining("")).length()
                        )
                ).entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey).orElse(null);
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream().flatMap(x -> x.entrySet().stream()).collect(Collectors.toList()).stream().
                collect(
                        Collectors.groupingBy(
                                Map.Entry::getKey,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        entries -> entries.stream().mapToInt(Map.Entry::getValue).sum()
                                )
                        )
                );
    }
}
