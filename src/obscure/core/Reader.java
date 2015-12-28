package obscure.core;

import java.io.IOException;

public class Reader {

    static final int EOF = -1;
    int ch;
    
    static boolean isSpace(int ch) {
        return Character.isWhitespace(ch);
    }
    
    static boolean isDigit(int ch) {
        return Character.isDigit(ch);
    }

    static boolean isFirstSymbol(int ch) {
        if (Character.isAlphabetic(ch)) return true;
        if (Character.isDigit(ch)) return false;
        if (ch <= ' ') return false;
        switch (ch) {
            case '.': case ';':
            case '"': case '\'':
            case '(': case ')':
            case '[': case ']':
            case '{': case '}':
                return false;
        }
        return true;
    }
    
    static boolean isRestSymbol(int ch) {
        if (isFirstSymbol(ch)) return true;
        if (isDigit(ch)) return true;
        if (ch == '.') return true;
        return false;
    }

    final java.io.Reader reader;
    
    public Reader(java.io.Reader reader) throws IOException {
        this.reader = reader;
        get();
    }
    
    int get() throws IOException {
        return ch = reader.read();
    }
    
    void skipSpace() throws IOException {
        while (isSpace(ch))
            get();
    }
    
    Obj readList() throws IOException {
        get();
        Pair.Builder b = Pair.builder();
        while (true) {
            skipSpace();
            switch (ch) {
                case ')':
                    get();
                    return b.build();
                case '.':
                    if (b.tail() == Nil.value)
                        throw new ObscureException("reader: unexpected '.' after ')'");
                    get();
                    b.end(read());
                    skipSpace();
                    if (ch != ')')
                        throw new ObscureException("reader: ')' expected");
                    get();
                    return b.build();
                default:
                    b.tail(read());
                    break;
            }
        }
    }
    
    Obj readQuote() throws IOException {
        get();
        return Pair.list(Symbol.QUOTE, read());
    }
    
    Obj readSymbol() throws IOException {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append((char)ch);
            get();
        } while (isRestSymbol(ch));
        return Symbol.of(sb.toString());
    }

    public Obj read() throws IOException {
        skipSpace();
        switch (ch) {
            case '(': return readList();
            case '\'': return readQuote();
            default:
                if (isFirstSymbol(ch))
                    return readSymbol();
                else
                    throw new ObscureException("reader: unknown charcter '%c'", ch);
        }
        
    }
}
