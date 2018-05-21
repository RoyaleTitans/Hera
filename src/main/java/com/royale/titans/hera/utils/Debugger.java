package com.royale.titans.hera.utils;

import com.royale.titans.hera.Configuration;

// TODO: Add colors
public class Debugger {
    public static void info(Object msg) {
        System.out.println("[ INFO  ] " + msg);
    }

    public static void debug(Object msg) {
        if (Configuration.DEBUG)
            System.out.println("[ DEBUG ] " + msg);
    }

    public static void warning(Object msg) {
        System.out.println("[WARNING] " + msg);
    }

    public static void error(Object msg) {
        System.err.println("[ ERROR ] " + msg);
    }
}
