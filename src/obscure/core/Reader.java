package obscure.core;

import java.io.IOException;
import java.io.StringReader;

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
            case '?': // case ';':
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
        if (ch == '.') return true;
        if (isDigit(ch)) return true;
        return false;
    }

    final java.io.Reader reader;
    
    public Reader(java.io.Reader reader) throws IOException {
        this.reader = reader;
        get();
    }
    
    public Reader(String s) throws IOException {
        this(new StringReader(s));
    }
    
    int get() throws IOException {
        return ch = reader.read();
    }
    
    void skipSpace() throws IOException {
        while (isSpace(ch))
            get();
    }
    
    Object readList() throws IOException {
        get();
        Pair.Builder b = new Pair.Builder();
        while (true) {
            skipSpace();
            switch (ch) {
                case ')':
                    get();
                    return b.build();
                case '.':
                    if (b.isEmpty())
                        throw new ObscureException("unexpected '.' after '('");
                    get();
                    b.end(read());
                    skipSpace();
                    if (ch != ')')
                        throw new ObscureException("')' expected");
                    get();
                    return b.build();
                default:
                    b.tail(read());
                    break;
            }
        }
    }
    
    Object readQuote() throws IOException {
        get();
        return Pair.list(Symbol.QUOTE, read());
    }
    
    char readEscape() throws IOException {
        char r;
        switch (get()) {
            case 'b': r = '\b'; break;
            case 'f': r = '\f'; break;
            case 't': r = '\t'; break;
            case 'n': r = '\n'; break;
            case 'r': r = '\r'; break;
            case '\"': r = '\"'; break;
            case '\\': r = '\\'; break;
            default: throw new ObscureException("unknown character ?\\%c", ch);
        }
        get();
        return r;
    }

    Object readString() throws IOException {
        get();
        StringBuilder sb = new StringBuilder();
        while (true) {
            switch (ch) {
                case '"': get(); return sb.toString();
                case '\\': sb.append(readEscape()); break;
                case '\b': case '\f': case '\r': case '\n':
                    throw new ObscureException("unterminated string literal %s", sb);
                default: sb.append((char)ch); get(); break;
            }
        }
    }

    Object readChar() throws IOException {
        switch (get()) {
            case '\\': return readEscape();
                case '\b': case '\f': case '\r': case '\n':
                throw new ObscureException("unterminated character literal");
            default:
                char r = (char)ch;
                get();
                return r;
        }
    }
    
    Object readSymbol() throws IOException {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append((char)ch);
            get();
        } while (isRestSymbol(ch));
        String s = sb.toString();
        if (s.equals("true")) return Boolean.TRUE;
        else if (s.equals("false")) return Boolean.FALSE;
        else if (s.equals("null")) return null;
        return Symbol.of(s);
    }
    
    Object readNumber() throws IOException {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append((char)ch);
            get();
        } while (isDigit(ch));
        if (Character.toUpperCase(ch) == 'L') {
            get();
            return Long.valueOf(sb.toString());
        } else
            return Integer.valueOf(sb.toString());
    }

    public Object read() throws IOException {
        skipSpace();
        switch (ch) {
            case '(': return readList();
            case '\'': return readQuote();
            case '"': return readString();
            case '?': return readChar();
            case EOF: throw new ObscureException("unexpected EOF");
            default:
                if (isFirstSymbol(ch))
                    return readSymbol();
                else if (isDigit(ch))
                    return readNumber();
                else
                    throw new ObscureException("unknown charcter '%s'", (char)ch);
        }
        
    }
}
