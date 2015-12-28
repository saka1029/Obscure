package obscure.core;

public class ObscureException extends RuntimeException {
    
    private static final long serialVersionUID = 7624427591429979236L;

    public ObscureException(String format, Object... args) {
        super(String.format(format, args));
        System.err.printf(format + "%n", args);
    }

}
