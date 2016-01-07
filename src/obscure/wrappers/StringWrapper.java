package obscure.wrappers;

import obscure.core.Procedure;
import obscure.core.Symbol;

public class StringWrapper extends AbstractWrapper {

    @Override
    public Class<?> wrapClass() {
        return String.class;
    }

    @Override
    public Class<?> parentClass() {
        return Object.class;
    }

    public StringWrapper() {
        map.put(Symbol.of("+"), (Procedure) ((self, args) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(self);
            for (Object e : args)
                sb.append(e);
            return sb.toString();
        }));
    }

    @Override
    public String print(Object self) {
        String s = (String)self;
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        for (int i = 0, l = s.length(); i < l; ++i) {
            char c = s.charAt(i);
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
        }
        sb.append("\"");
        return sb.toString();
    }
}
