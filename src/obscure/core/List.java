package obscure.core; 

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class List implements Evalable, Iterable<Object> {

    public abstract Object car();
    public abstract Object cdr();
    public abstract boolean isPair();
    
    @Override
    public Iterator<Object> iterator() {
        return new Iterator<Object>() {
            
            List list = List.this;
            
            @Override
            public boolean hasNext() {
                return list instanceof Pair;
            }

            @Override
            public Object next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                Object car = list.car();
                Object cdr = list.cdr();
                list = cdr instanceof List ? (List)cdr : null;
                return car;
            }
            
        };
    }
    
}
