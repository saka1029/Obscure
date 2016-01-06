package obscure.core;

public class Closure implements Procedure {
 
    final Object parms;
    final List body;
    final Env env;

    public Closure(Object parms, List body, Env env) {
        this.parms = parms;
        this.body = body;
        this.env = env;
    }
    
    static boolean isPair(Object obj) {
        return obj instanceof Pair;
    }
    
    static List asList(Object obj) {
        return (List)obj;
    }

    static Env pairlis(Object parms, List args, Env env) {
        Env n = Env.create(env);
        Object p = parms;
        Object a = args;
        for (; isPair(p); p = asList(p).cdr(), a = asList(args).cdr())
            n.define((Symbol)asList(p).car(), asList(a).car());
        if (p instanceof Symbol)
            n.define((Symbol)p, a);
        return n;
    }

    @Override
    public Object apply(Object self, List args) {
        Object r = false;
        Env n = pairlis(parms, args, env);
        for (Object e : body)
            r = Global.eval(e, n);
        return r;
    }

}