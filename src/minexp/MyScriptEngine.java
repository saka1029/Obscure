package minexp;

//MyScriptEngine.java

import java.io.*;

import javax.script.*;

/**
 * expression := term | term ( ’+’ | ’-’ ) expression
 * term := factor | factor ( ’*’ | ’/’ | ’%’ ) term
 * factor := number | ’-’ factor | ’(’ expression ’)’
 * number := digit | digit number
 * digit := ( ’0’ | ’1’ | ’2’ | ’3’ | ’4’ | ’5’ | ’6’ | ’7’ | ’8’ | ’9’ )
 */
public class MyScriptEngine extends AbstractScriptEngine {

    public Bindings createBindings() {
        return null; // Uninitialized bindings not needed because Minexp does
        // not support bindings.
    }

    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        System.out.println("eval(): " + context);
        if (reader == null || context == null)
            throw new NullPointerException();
        StringBuffer sb = new StringBuffer(50); // Assume scripts <= 50 chars
        try {
            int ch;
            while ((ch = reader.read()) != -1)
                sb.append((char) ch);
        } catch (IOException e) {
            throw new ScriptException("Unable to read stream", "<unknown>", -1, -1);
        }
        return eval(sb.toString(), context);
    }

    public Object eval(String script, ScriptContext context) throws ScriptException {
        System.out.println("eval(" + script + "): " + context + " writer=" + context.getWriter());
        if (script == null || context == null)
            throw new NullPointerException();
        Tokenizer t = new Tokenizer(script);
        int i = expr(t);
        if (t.getType() != Tokenizer.EOS)
            throw new ScriptException("Extra characters: " + t.getToken(), "<unknown>", -1, t.getPos() - 1);
        return i;
    }

    public ScriptEngineFactory getFactory() {
        return new MyScriptEngineFactory();
    }

    private int expr(Tokenizer t) throws ScriptException {
        int res = term(t);
        String tok = t.getToken();
        while (tok.equals("+") || tok.equals("-")) {
            switch (tok) {
                case "+": res += term(t); break;
                case "-": res -= term(t); break;
            }
            tok = t.getToken();
        }
        return res;
    }

    private int term(Tokenizer t) throws ScriptException {
        int res = factor(t);
        String tok = t.getToken();
        while (tok.equals("*") || tok.equals("/") || tok.equals("%")) {
            switch (tok) {
                case "*": res *= factor(t); break;
                case "/":
//                    try {
                        res /= factor(t);
//                    } catch (ArithmeticException e) {
//                        throw new ScriptException("Divide by zero", "<unknown>", -1, t.getPos() - 1);
//                        throw new ScriptException(e);
//                    }
                    break;
                case "%":
//                    try {
                        res %= factor(t);
//                    } catch (ArithmeticException e) {
//                        throw new ScriptException("Divide by zero", "<unknown>", -1, t.getPos() - 1);
//                        throw new ScriptException(e);
//                    }
                    break;
            }
            tok = t.getToken();
        }
        return res;
    }

    private int factor(Tokenizer t) throws ScriptException {
        t.nextToken();
        String tok = t.getToken();
        switch (t.getType()) {
            case Tokenizer.NUMBER:
                try {
                    int i = Integer.parseInt(tok);
                    t.nextToken();
                    return i;
                } catch (NumberFormatException e) {
                    throw new ScriptException("Invalid number: " + tok, "<unknown>", -1, t.getPos() - 1);
                }
            default:
                switch (tok) {
                    case "-": return -factor(t);
                    case "(":
                        int res = expr(t);
                        tok = t.getToken();
                        if (!tok.equals(")"))
                            throw new ScriptException("Missing )", "<unknown>", -1, t.getPos());
                        t.nextToken();
                        return res;
                }
                
        }
        if (t.getType() == Tokenizer.EOS)
            throw new ScriptException("Missing token", "<unknown>", -1, t.getPos());
        else
            throw new ScriptException("Invalid token: " + tok, "<unknown>", -1, t.getPos() - 1);
    }

//    private ScriptContext context = new SimpleScriptContext();
//    
//    @Override
//    public ScriptContext getContext() {
//        System.out.println("getContext()");
//        return this.context;
//    }
//    
//    @Override
//    public void setContext(ScriptContext context) {
//        System.out.println("setContext()");
//        this.context = context;
//    }
}