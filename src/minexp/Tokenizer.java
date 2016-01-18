package minexp;

public class Tokenizer {

    final static int EOS = 0; // end of string
    final static int NUMBER = 1; // integer
    final static int OTHER = 2; // single character
    private String text, token;
    private int len, pos, type;

    Tokenizer(String text) {
        this.text = text;
        len = text.length();
        pos = 0;
    }

    int getPos() {
        return pos;
    }

    String getToken() {
        return token;
    }

    int getType() {
        return type;
    }

    void nextToken() {
        while (pos < len && Character.isWhitespace(text.charAt(pos)))
            pos++;
        if (pos < len && Character.isDigit(text.charAt(pos))) {
            StringBuffer sb = new StringBuffer();
            do {
                sb.append(text.charAt(pos++));
            } while (pos < len && Character.isDigit(text.charAt(pos)));
            type = NUMBER;
            token = sb.toString();
            return;
        }
        if (pos < len) {
            token = "" + text.charAt(pos++);
            type = OTHER;
        } else {
            token = "";
            type = EOS;
        }
    }
}
