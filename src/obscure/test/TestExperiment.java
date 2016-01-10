package obscure.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;

public class TestExperiment {

    interface A { void a(); }
    class B { public void b() {} }
    class C extends B implements A { public void a() {} }

    @Test
    public void testMethodSearch() {
        for (Method m : C.class.getMethods())
            System.out.println(m.getDeclaringClass() + " " + m);
    }
    
    public void intArg(int i) {
        System.out.println("i=" + i);
    }

    @Test
    public void testCallFooInt() throws Exception {
        getClass().getMethod("intArg", Integer.TYPE).invoke(this, new Integer(100));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCallFooLong() throws Exception {
        getClass().getMethod("intArg", Integer.TYPE).invoke(this, new Long(100));
    }

    @Test(expected = ClassCastException.class)
    public void testCastIntegerToInt() {
        System.out.println(Integer.TYPE.cast(18));
    }
    
    @Test
    public void testIsAssignableFrom() {
        System.out.println(Integer.class.isAssignableFrom(Long.class));
        System.out.println(Long.class.isAssignableFrom(Integer.class));
        System.out.println(Long.TYPE.isAssignableFrom(Integer.TYPE));
        System.out.println(Integer.TYPE.isAssignableFrom(Integer.class));
        System.out.println(Integer.class.isAssignableFrom(Integer.TYPE));
    }

    @Test
    public void testLambdaThis() {
        Function<Integer, Integer> f = x -> {
            System.out.println(this.getClass().getName());
            System.out.println(Arrays.toString(this.getClass().getInterfaces()));
            return x + 1;
        };
        f.apply(3);
        new Function<Integer, Integer>() {
            @Override public Integer apply(Integer x) {
                System.out.println(this.getClass().getName());
                System.out.println(Arrays.toString(this.getClass().getInterfaces()));
                return x + 1;
            }
        }.apply(3);
            
    }
}
