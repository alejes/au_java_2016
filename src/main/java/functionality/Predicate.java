package functionality;

/**
 * Created by Alexey on 18.03.2016.
 */
public interface Predicate<T> extends Function1<T, Boolean> {

    Predicate<Object> ALWAYS_TRUE = arg -> true;
    Predicate<Object> ALWAYS_FALSE = arg -> false;

    default Predicate<T> or(Predicate<? super T> other) {
        return arg -> apply(arg) || other.apply(arg);
    }

    default Predicate<T> and(Predicate<? super T> other) {
        return arg -> apply(arg) && other.apply(arg);
    }

    default Predicate<T> not() {
        return arg -> !apply(arg);
    }

}
