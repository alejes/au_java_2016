package functionality;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Collections {
    public static <A, R> List<R> map(Function1<? super A, R> f, Iterable<A> it) {
        List<R> lst = new ArrayList<>();
        for (A el : it) {
            lst.add(f.apply(el));
        }
        return lst;
    }

    public static <A> List<A> filter(Predicate<? super A> p, Iterable<A> it) {
        List<A> lst = new ArrayList<>();
        for (A el : it) {
            if (p.apply(el)) {
                lst.add(el);
            }
        }
        return lst;
    }

    public static <A> List<A> takeWhile(Predicate<? super A> p, Iterable<A> it) {
        List<A> lst = new ArrayList<>();
        for (A el : it) {
            if (!p.apply(el)) {
                break;
            }
            lst.add(el);
        }
        return lst;
    }

    public static <A> List<A> takeUnless(Predicate<? super A> p, Iterable<A> it) {
        return takeWhile(p.not(), it);
    }

    public static <A, R> R foldl(Function2<R, ? super A, R> f, R init, Iterable<A> it) {
        for (A el : it) {
            init = f.apply(init, el);
        }
        return init;
    }

    public static <A, R> R foldr(Function2<? super A, R, R> f, R init, Iterable<A> it) {
        return foldrDetails(f, init, it.iterator());
    }

    private static <A, R> R foldrDetails(Function2<? super A, R, R> f, R init, Iterator<A> iter) {
        if (iter.hasNext()) {
            A el = iter.next();
            return f.apply(el, foldrDetails(f, init, iter));
        } else return init;
    }


}
