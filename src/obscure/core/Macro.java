package obscure.core;

public interface Macro extends Applicable {
    
    Object expand(List args, Env env);
    
    @Override
    default Object apply(Object self, List args, Env env) {
        Object expanded = expand(args, env);
        System.out.println("expanded: " + Global.print(expanded));
        return Global.eval(expanded, env);
    }

}
