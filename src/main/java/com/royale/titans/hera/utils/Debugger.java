package com.royale.titans.hera.utils;

import com.royale.titans.hera.Configuration;

public class Debugger {
    public static void info(Object msg) {
        System.out.println(Colors.WHITE_BOLD + "[ INFO  ] " + Colors.RESET + Colors.WHITE + msg);
    }

    public static void debug(Object msg) {
        if (Configuration.DEBUG)
            System.out.println(Colors.CYAN_BOLD + "[ DEBUG ] " + Colors.RESET + Colors.CYAN + msg);
    }

    public static void warning(Object msg) {
        System.out.println(Colors.YELLOW_BOLD + "[WARNING] " + Colors.RESET + Colors.YELLOW + msg);
    }

    public static void error(Object msg) {
        System.out.println(Colors.RED_BOLD + "[ ERROR ] " + Colors.RESET + Colors.RED + msg);
    }

    private static class Colors {
        public static final String RESET = "\033[0m";

        public static final String WHITE = "\033[0;97m";
        public static final String WHITE_BOLD = "\033[1;97m";
        public static final String CYAN = "\033[0;36m";
        public static final String CYAN_BOLD = "\033[1;36m";
        public static final String YELLOW = "\033[0;93m";
        public static final String YELLOW_BOLD = "\033[1;93m";
        public static final String RED = "\033[0;91m";
        public static final String RED_BOLD = "\033[1;91m";
    }
}
