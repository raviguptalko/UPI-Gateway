package com.treatpay.upigateway;

public class Wrap {
    static {
        System.loadLibrary("native-lib");
    }
    public static native String t1();
    public static native String t2();
    public static native String t3();
    public static native String t4();
    public static native String t5();
    public static native String t6();
    public static native String t7();
    public static native String t8();
    public static native String t9();
    public static native String t10();
    public static native String t11();
    public static native String t12();
    public static native String t13();
    public static native int enc(int a);
    public static native String r1(int a);
    public static native String r2(String pa, String pn, String tn, String am, String tr);
}
