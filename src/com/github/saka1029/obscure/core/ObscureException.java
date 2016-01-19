package com.github.saka1029.obscure.core;

public class ObscureException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public ObscureException(Throwable t) {
        super(t);
    }
    
    public ObscureException(String format, Object... args) {
        super(String.format(format, args));
    }

}
