package com.github.saka1029.obscure.test;

import static com.github.saka1029.obscure.core.Global.*;

public class Debug implements AutoCloseable {

    final long start;
    final String name;
    
    public Debug(String name) {
        this.name = name;
        start = System.currentTimeMillis();
        DEBUG = true;
        System.out.printf("Debug: %s start.%n", name);
    }

    @Override
    public void close() {
        DEBUG = false;
        System.out.printf("Debug: %s end. elapse=%dmsec.%n", name, System.currentTimeMillis() - start);
    }

}
