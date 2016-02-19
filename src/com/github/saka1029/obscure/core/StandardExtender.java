package com.github.saka1029.obscure.core;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Arrays;

public class StandardExtender {
    
    private StandardExtender() {}
    
    // array
    public static Object set(Object array, int index, Object value) { Object r = value; Array.set(array, index, value); return r;}
    public static Object get(Object array, int index) { return Array.get(array, index); }
    public static int length(Object array) { return Array.getLength(array); }
    public static String print(Object object) {
        if (object == null)
            return "null";
        if (object.getClass().isArray()) {
            Object[] array;
            if (object.getClass().getComponentType().isPrimitive()) {
                int length = Array.getLength(object);
                array = (Object[])Array.newInstance(Object.class, length);
                for (int i = 0; i < length; ++i)
                    array[i] = Array.get(object, i);
            } else
                array = (Object[])object;
            return Arrays.deepToString(array);
        } else
            return object.toString();
        }
    
    // int
//    @ObscureName("+") public static int add(int a, byte b) { return a + b; }
//    @ObscureName("+") public static int add(int a, char b) { return a + b; }
//    @ObscureName("+") public static int add(int a, short b) { return a + b; }
    @ObscureName("+") public static int add(int a, int b) { return a + b; }
//    @ObscureName("+") public static long add(int a, long b) { return a + b; }
//    @ObscureName("+") public static double add(int a, double b) { return a + b; }
    @ObscureName("-") public static int subtract(int a, int b) { return a - b; }
//    @ObscureName("-") public static long subtract(int a, long b) { return a - b; }
    @ObscureName("*") public static int multiply(int a, int b) { return a * b; }
//    @ObscureName("*") public static long multiply(int a, long b) { return a * b; }
    @ObscureName("<0") public static boolean lt0(int a) { return a < 0; }
    @ObscureName("<=0") public static boolean le0(int a) { return a <= 0; }
    @ObscureName(">0") public static boolean gt0(int a) { return a > 0; }
    @ObscureName(">=0") public static boolean ge0(int a) { return a >= 0; }
    @ObscureName("!=0") public static boolean ne0(int a) { return a != 0; }
    @ObscureName("==0") public static boolean eq0(int a) { return a == 0; }

    // long
//    @ObscureName("+") public static long add(long a, int b) { return a + b; }
    @ObscureName("+") public static long add(long a, long b) { return a + b; }
//    @ObscureName("-") public static long subtract(long a, int b) { return a - b; }
    @ObscureName("-") public static long subtract(long a, long b) { return a - b; }
//    @ObscureName("*") public static long multiply(long a, int b) { return a * b; }
    @ObscureName("*") public static long multiply(long a, long b) { return a * b; }
    public static String print(long a) { return Long.toString(a) + "L"; }
    
    // BigInteger
//    @ObscureName("+") public static BigInteger add(int a, BigInteger b) { return BigInteger.valueOf(a).add(b); }
    @ObscureName("+") public static BigInteger add(BigInteger a, BigInteger b) { return a.add(b); }
    @ObscureName("-") public static BigInteger subtract(BigInteger a, BigInteger b) { return a.subtract(b); }
    @ObscureName("*") public static BigInteger multiply(BigInteger a, BigInteger b) { return a.multiply(b); }
    @ObscureName("/") public static BigInteger divide(BigInteger a, BigInteger b) { return a.divide(b); }
    public static String print(BigInteger a) { return a.toString() + "I"; }

    // String
    @ObscureName("+") public static String add(String a, Object b) { return a + b; }
    public static String print(String s) {
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
