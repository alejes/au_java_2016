package functionality;

public interface Function1<A, R> { // f

    R apply(A arg);

    default <R1> Function1<A, R1> compose(Function1<? super R, R1> g) {
        // g(f(x))
        return arg -> g.apply(apply(arg));
    }
}