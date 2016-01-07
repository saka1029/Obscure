package obscure.wrappers;

import obscure.core.Procedure;
import obscure.core.Symbol;

public class CharacterWrapper extends AbstractWrapper {

    @Override
    public Class<?> wrapClass() {
        return Character.class;
    }

    @Override
    public Class<?> parentClass() {
        return Object.class;
    }

    public CharacterWrapper() {
    }

    @Override
    public String print(Object self) {
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        char c = (char)self;
        switch (c) {
            case '\"': sb.append("\\\""); break;
            case '\\': sb.append("\\\\"); break;
            case '\b': sb.append("\\b"); break;
            case '\f': sb.append("\\f"); break;
            case '\t': sb.append("\\t"); break;
            case '\n': sb.append("\\n"); break;
            case '\r': sb.append("\\r"); break;
            default: sb.append(c); break;
        }
        return sb.toString();
    }
}
