package obscure.core;

public class ListHelper {

    private ListHelper() {}
    
    public static boolean isList(Object obj) {
        return obj instanceof List;
    }
    
    public static boolean isPair(Object obj) {
        return obj instanceof Pair;
    }
    
    public static List asList(Object obj) {
        if (!(obj instanceof List))
            throw new ObscureException("cannot cast to List: %s", obj);
        return (List)obj;
    }

    public static Object car(Object obj) {
        return asList(obj).car();
    }

    public static Object cdr(Object obj) {
        return asList(obj).cdr();
    }
    
    public static Object cadr(Object obj) {
        return car(cdr(obj));
    }

    public static Object caddr(Object obj) {
        return car(cdr(cdr(obj)));
    }

    public static Object cadddr(Object obj) {
        return car(cdr(cdr(cdr(obj))));
    }

    public static Pair cons(Object car, Object cdr) {
        return Pair.of(car, cdr);
    }
    
    public static List list(Object... args) {
        return Pair.list(args);
    }
    
}
