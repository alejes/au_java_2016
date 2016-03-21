package functionality;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Collections {
    public static <A, R> List<R> map(Function1<? super A, R> f, Iterable<A> it) {
        List<R> lst = new ArrayList<>();
        for (A el : it){
            lst.add(f.apply(el));
        }
        return lst;
    }

    public static <A> List<A> filter(Predicate<? super A> p, Iterable<A> it) {
        List<A> lst = new ArrayList<>();
        for (A el : it){
            if (p.apply(el)) {
                lst.add(el);
            }
        }
        return lst;
    }

    public static <A> List<A> takeWhile(Predicate<? super A> p, Iterable<A> it) {
        List<A> lst = new ArrayList<>();
        for (A el : it){
            if (!p.apply(el)) {
                break;
            }
            lst.add(el);
        }
        return lst;
    }

    public static <A> List<A> takeUnless(Predicate<? super A> p, Iterable<A> it) {
        List<A> lst = new ArrayList<>();
        for (A el : it){
            if (p.apply(el)) {
                break;
            }
            lst.add(el);
        }
        return lst;
    }

    public static <A, R> R foldl(Function2<? super R, ? super  A,  ? extends R > f,  R init, Iterable<A> it) {
        for (A el : it){
            init = f.apply(init, el);
        }
        return init;
    }

    private static <A, R> R foldr_use(Function2<? super A, ? super R,  ? extends R >  f,  R init, Iterator<A> iter) {
        if (iter.hasNext()){
            A el = iter.next();
            return f.apply(el, foldr_use(f, init, iter));
        }
        else return init;
    }

    public static <A, R> R foldr(Function2<? super A, ? super R, ? extends R > f,  R init, Iterable<A> it) {
        return foldr_use(f, init, it.iterator());
    }
}
