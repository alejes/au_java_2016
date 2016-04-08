package ru.spbau.stepanov.linq;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class FirstPartTasks {

    private FirstPartTasks() {
    }

    // Список названий альбомов
    public static List<String> allNames(Stream<Album> albums) {
        return albums.map(Album::getName).collect(Collectors.toList());
    }

    // Список названий альбомов, отсортированный лексикографически по названию
    public static List<String> allNamesSorted(Stream<Album> albums) {
        return albums.map(Album::getName).sorted().collect(Collectors.toList());
    }

    // Список треков, отсортированный лексикографически по названию, включающий все треки альбомов из 'albums'
    public static List<String> allTracksSorted(Stream<Album> albums) {
        return albums.flatMap(alb -> alb.getTracks().stream()).map(Track::getName).sorted().collect(Collectors.toList());
    }

    // Список альбомов, в которых есть хотя бы один трек с рейтингом более 95, отсортированный по названию
    public static List<Album> sortedFavorites(Stream<Album> s) {
        //return s.map(alb -> alb.getTracks().stream()).filter(track -> track.anyMatch(x -> true)).flatMap(Function.identity()).sorted().collect(Collectors.toList());
        //return s.collect(Collectors.groupingBy(Function.identity())).entrySet().stream().map((x, y)-> Map.Entry(x, ((List<Album>) x).stream())).
        //x -> x.getTracks().stream()
        return s.filter(alb -> alb.getTracks().stream().anyMatch(x -> x.getRating() > 95)).sorted((x, y) -> x.getName().compareTo(y.getName())).collect(Collectors.toList());
    }

    // Сгруппировать альбомы по артистам
    public static Map<Artist, List<Album>> groupByArtist(Stream<Album> albums) {
        return albums.collect(Collectors.groupingBy(Album::getArtist));
    }

    // Сгруппировать альбомы по артистам (в качестве значения вместо объекта 'Artist' использовать его имя)
    public static Map<Artist, List<String>> groupByArtistMapName(Stream<Album> albums) {
        //return albums.collect(Collectors.groupingBy(album -> album.getName(), Collectors.mapping(Album::getArtist, Collectors.toList())));
        //album -> album.getName()
        //return albums.collect(Collectors.groupingBy(Function.identity(), Collectors.mapping(album -> album.getArtist().getName(), Collectors.toList())));
        /*
        return albums.collect(
                Collectors.groupingBy(
                        Function.identity(),
                        Collectors.mapping(
                                album -> album.getArtist().getName(),
                                Collectors.collectingAndThen(
                                        Collectors.toList(), Function.identity()
                                ))));
                                */
        return albums.collect(
                Collectors.groupingBy(
                        Album::getArtist,
                        Collectors.mapping(
                                album -> album.getName(),
                                Collectors.collectingAndThen(
                                        Collectors.toList(), Function.identity()
                                )
                        )
                ));
    }

    // Число повторяющихся альбомов в потоке
    public static long countAlbumDuplicates(Stream<Album> albums) {
        class Holder {
            long value = 0;

            void increment() {
                value += 1;
            }
        }
        Holder hold = new Holder();
        long dist = albums.peek(e -> hold.increment()).distinct().count();
        //Collectors.groupingBy(Phone::getCompany, Collectors.summingInt(Phone::getPrice)));
        return hold.value - dist;
    }

    // Альбом, в котором максимум рейтинга минимален
    // (если в альбоме нет ни одного трека, считать, что максимум рейтинга в нем --- 0)
    public static Optional<Album> minMaxRating(Stream<Album> albums) {

        return albums.collect(
                Collectors.groupingBy(
                        Function.identity(),
                        Collectors.mapping(
                                album -> album.getTracks().stream().mapToInt(Track::getRating).max().orElse(0),
                                Collectors.collectingAndThen(
                                        Collectors.toList(), trackList -> trackList.get(0)
                                )))).entrySet().stream().sorted((o1, o2) -> o1.getValue().compareTo(o2.getValue())).findFirst().map(Map.Entry::getKey);
    }

    // Список альбомов, отсортированный по убыванию среднего рейтинга его треков (0, если треков нет)
    public static List<Album> sortByAverageRating(Stream<Album> albums) {

        //Object xxxxxx = albums.sorted(album -> album.getTracks().stream().mapToInt(Track::getRating).average().orElse(0)).collect(Collectors.toList());
        //Object xxxxxx = albums.sorted(Comparator.comparing(o1 -> ((Album)o1).getTracks().stream().mapToInt(Track::getRating).average().orElse(0)).reversed());

        return albums.sorted(Comparator.comparing(o1 -> ((Album) o1).getTracks().stream().mapToInt(Track::getRating).average().orElse(0)).reversed()).collect(Collectors.toList());
    }

    // Произведение всех чисел потока по модулю 'modulo'
    // (все числа от 0 до 10000)
    public static int moduloProduction(IntStream stream, int modulo) {
        return stream.reduce(1, (left, right) -> left * right % modulo);
    }

    // Вернуть строку, состояющую из конкатенаций переданного массива, и окруженную строками "<", ">"
    // см. тесты
    public static String joinTo(String... strings) {
        //return "<" + Stream.of(strings).reduce((s, s2) -> s + ", " + s2).orElse("") + ">";
        return Stream.of(strings).collect(Collectors.joining(", ", "<", ">"));
    }

    // Вернуть поток из объектов класса 'clazz'
    public static <R> Stream<R> filterIsInstance(Stream<?> s, Class<R> clazz) {
        return s.filter(clazz::isInstance).map(o -> clazz.cast(o));
    }
}