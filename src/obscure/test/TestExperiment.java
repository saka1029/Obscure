package obscure.test;

import static org.junit.Assert.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
    
    static Map<Class<?>, Class<?>> PRIM = new HashMap<>();
    static {
        PRIM.put(Byte.class, Byte.TYPE);
        PRIM.put(Character.class, Character.TYPE);
        PRIM.put(Short.class, Short.TYPE);
        PRIM.put(Integer.class, Integer.TYPE);
        PRIM.put(Long.class, Long.TYPE);
        PRIM.put(Float.class, Float.TYPE);
        PRIM.put(Double.class, Double.TYPE);
        PRIM.put(Void.class, Void.TYPE);
    }
    
    static Class<?>[] argTypes(Object... args) {
        Class<?>[] r = new Class[args.length];
        int i = 0;
        for (Object o : args) {
            Class<?> c = o.getClass();
            if (PRIM.containsKey(c))
                c = PRIM.get(c);
            r[i++] = c;
        }
        return r;
    }

    static Object[] cons(Object self, Object...args) {
        Object[] r = new Object[args.length + 1];
        r[0] = self;
        int i = 1;
        for (Object o : args)
            r[i++] = o;
        return r;
    }

    static Object invoke(Object self, String name, Object... args) throws Throwable {
        Class<?> selfClass = self.getClass();
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType mt = MethodType.methodType(selfClass, argTypes(args));   // return type, arguments' types
        MethodHandle mh = lookup.findVirtual(selfClass, name, mt);  // this type, method name, signature
        return mh.invokeWithArguments(cons(self, args));
    }

    @Test
    public void testInvoke() throws Throwable {
//        MethodHandles.Lookup lookup = MethodHandles.lookup();
//        MethodType mt = MethodType.methodType(String.class, char.class, char.class);
//        MethodHandle mh = lookup.findVirtual(String.class, "replace", mt);
        assertEquals("nanny", invoke("daddy", "replace", 'd', 'n'));
        assertEquals("abcdef", invoke("abc", "concat", "def"));
//        assertEquals(3, invoke("abc", "length"));
    }
}
