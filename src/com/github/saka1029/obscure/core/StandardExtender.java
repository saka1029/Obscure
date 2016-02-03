package com.github.saka1029.obscure.core;

import java.math.BigInteger;

public class StandardExtender {
    
    private StandardExtender() {}
    
    // int
    @MethodName("+") public static int add(int a, byte b) { return a + b; }
    @MethodName("+") public static int add(int a, char b) { return a + b; }
    @MethodName("+") public static int add(int a, short b) { return a + b; }
    @MethodName("+") public static int add(int a, int b) { return a + b; }
    @MethodName("+") public static long add(int a, long b) { return a + b; }
    @MethodName("+") public static double add(int a, double b) { return a + b; }
    @MethodName("-") public static int subtract(int a, int b) { return a - b; }
    @MethodName("-") public static long subtract(int a, long b) { return a - b; }
    @MethodName("*") public static int multiply(int a, int b) { return a * b; }
    @MethodName("*") public static long multiply(int a, long b) { return a * b; }
    @MethodName("<0") public static boolean lt0(int a) { return a < 0; }
    @MethodName("<=0") public static boolean le0(int a) { return a <= 0; }
    @MethodName(">0") public static boolean gt0(int a) { return a > 0; }
    @MethodName(">=0") public static boolean ge0(int a) { return a >= 0; }
    @MethodName("<>0") public static boolean ne0(int a) { return a != 0; }
    @MethodName("==0") public static boolean eq0(int a) { return a == 0; }

    // long
    @MethodName("+") public static long add(long a, int b) { return a + b; }
    @MethodName("+") public static long add(long a, long b) { return a + b; }
    @MethodName("-") public static long subtract(long a, int b) { return a - b; }
    @MethodName("-") public static long subtract(long a, long b) { return a - b; }
    @MethodName("*") public static long multiply(long a, int b) { return a * b; }
    @MethodName("*") public static long multiply(long a, long b) { return a * b; }
    @MethodName("print") public static String print(long a) { return Long.toString(a) + "L"; }
    
    // BigInteger
    @MethodName("+") public static BigInteger add(int a, BigInteger b) { return BigInteger.valueOf(a).add(b); }

    // String
    @MethodName("+") public static String add(String a, Object b) { return a + b; }
    @MethodName("print") public static String print(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        for (int i = 0, size = s.length(); i < size; ++i) {
            char c = s.charAt(i);
            switch (c) {
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\t': sb.append("\\t"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '"': sb.append("\\\""); break;
                default: sb.append(c); break;
            }
        }
        sb.append("\"");
        return sb.toString();
    }

}
