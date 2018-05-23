package com.royale.titans.hera.crypto.sodium.nacl;

import com.neilalexander.jnacl.crypto.curve25519xsalsa20poly1305;

public class KeyPair {
    public byte[] sk = new byte[32], pk = new byte[32];

    public KeyPair() {
        curve25519xsalsa20poly1305.crypto_box_keypair(pk, sk);
    }
}
