package minexp;

import java.util.*;

import javax.script.*;

public class MyScriptEngineFactory implements ScriptEngineFactory {

    public String getEngineName() {
        return "My Scripting Engine for Minexp";
    }

    public String getEngineVersion() {
        return "1.0";
    }

    public List<String> getExtensions() {
        return Arrays.asList("me");
    }

    public String getLanguageName() {
        return "Minexp";
    }

    public String getLanguageVersion() {
        return "0.1";
    }

    public String getMethodCallSyntax(String obj, String m, String... args) {
        return null; // Minexp has no methods
    }

    public List<String> getMimeTypes() {
        return Arrays.asList("text/Minexp"); // Illustration only -- not official
    }

    /**
     * これがjrunscriptコマンド実行時の-lオプションで指定する言語の名前。
     * getLanguageName()ではない点に注意する。
     */
    public List<String> getNames() {
        return Arrays.asList("Minexp");
    }

    public String getOutputStatement(String toDisplay) {
        return null; // Minexp has no I/O capability
    }

    public Object getParameter(String key) {
        // I’m not sure what to do with ScriptEngine.ARGV and
        // ScriptEngine.FILENAME -- not even Rhino JavaScript recognizes these
        // keys.

        switch (key) {
            case ScriptEngine.ENGINE: return getEngineName();
            case ScriptEngine.ENGINE_VERSION: return getEngineVersion();
            case ScriptEngine.NAME: return getNames().get(0);
            case ScriptEngine.LANGUAGE: return getLanguageName();
            case ScriptEngine.LANGUAGE_VERSION: return getLanguageVersion();
            case "THREADING": return null; // Until thoroughly tested.
        default: return null;
        }
    }

    public String getProgram(String... statements) {
        return null; // Minexp does not understand statements
    }

    public ScriptEngine getScriptEngine() {
        return new MyScriptEngine();
    }
}