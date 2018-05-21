package com.royale.titans.hera;

import com.royale.titans.hera.utils.binary.Hex;

public class Configuration {
    public static final boolean DEBUG = true;

    public static final int KEY_VERSION = 16;

    public static final boolean DOWNLOAD_MODE = false;
    public static final boolean ENABLE_BLACKLIST = true;

    public static final String CDN_URL = "https://99faf1e355c749a9a049-2a63f4436c967aa7d355061bd0d924a1.ssl.cf1.rackcdn.com";

    public static final byte[] NONCE = Hex.toBytes("8907a714cd1042e96daf7b9ad910c4cb2e34b2414fd5819f");
    public static final byte[] PRIVATE_KEY = Hex.toBytes("fb523187d9e4dcdb0a136e6a77a677de7a7983b9166eb1604f8f24aeecd750b3");
    public static final byte[] SERVER_KEY = Hex.toBytes("8cfd11687a20d616f0b7dc0ceed00ce12f5f95e2e100c9ff561b0c4117e6e44d");
}
