package com.royale.titans.hera.crypto.sodium.nacl;

public class CustomNaCl {
    public static byte[] openPublicBox(byte[] c, byte[] n, byte[] sk, byte[] pk) {
        return new PublicBox(sk, pk).open(c, n);
    }

    public static byte[] createPublicBox(byte[] p, byte[] n, byte[] sk, byte[] pk) {
        return new PublicBox(sk, pk).create(p, n);
    }

    public static byte[] openSecretBox(byte[] c, byte[] n, byte[] s) {
        return new SecretBox(s).open(c, n);
    }

    public static byte[] createSecretBox(byte[] p, byte[] n, byte[] s) {
        return new SecretBox(s).create(p, n);
    }
}
