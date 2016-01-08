package obscure.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestGenericFunctionInterface {

    static GenericFunctionInterface<String, String, String> impl = 
            new GenericFunctionInterface<String, String, String>() {
                
                @Override
                public String apply(String t, String u) {
                    return String.format("apply 2: %s %s", t, u);
                }
                
                @Override
                public String apply(String t) {
                    return String.format("apply 1: %s", t);
                }
            };
    @Test
    public void test() {
        assertEquals("apply 1: t", impl.apply("t"));
        assertEquals("apply 1: t.andThen", impl.andThen(s -> s + ".andThen").apply("t"));
        assertEquals("apply 2: t u", impl.apply("t", "u"));
    }
    
    interface A {}
    interface B {}
    interface AB extends A, B {}
    interface AA { A foo(); }
    interface BB { B foo(); }
    interface X extends AA, BB {
        @Override public AB foo();
    }

}
