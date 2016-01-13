package io.github.saka1029.obscure.core;

import java.util.List;

public interface Applicable {

    Object apply(Object self, List<Object> args, Environment env);

}
