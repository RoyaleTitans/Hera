package com.royale.titans.hera.core;

import com.royale.titans.hera.definitions.Fingerprint;

import java.io.File;

public class Resources {
    public static Fingerprint fingerprint;
    public static String[] version;

    public static void init() {
        if (new File("fingerprint.json").exists()) {
            Resources.fingerprint = Fingerprint.read();
            Resources.version = Resources.fingerprint.getVersion().replace(".", ",").split(",");
        }

        Client.connect();
    }
}
