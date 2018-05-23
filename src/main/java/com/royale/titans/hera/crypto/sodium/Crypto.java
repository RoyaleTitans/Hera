package com.royale.titans.hera.crypto.sodium;

import com.royale.titans.hera.Configuration;
import com.royale.titans.hera.core.Client;
import com.royale.titans.hera.crypto.sodium.nacl.CustomNaCl;
import com.royale.titans.hera.crypto.sodium.nacl.KeyPair;
import com.royale.titans.hera.utils.binary.ByteStream;
import com.royale.titans.hera.utils.binary.Hex;
import ove.crypto.digest.Blake2b;

import java.util.Random;

public class Crypto {
    private static int keyLength = 32, nonceLength = 24, sessionLength = 24;

    // A custom keypair used for en/decryption
    private static KeyPair keyPair = new KeyPair();

    // The 32-byte prefixed public key from cipher 10101
    private static byte[] _10101_PublicKey = new byte[keyLength];

    // The 24-byte prefixed nonce from plain 10101
    private static byte[] _10101_Nonce = new byte[nonceLength];

    // The 24-byte prefixed nonce from plain 20103/20104
    private static byte[] _20103_20104_Nonce = new byte[nonceLength];

    // The 32-byte prefixed shared key from plain 20103/20104
    private static byte[] _20103_20104_SharedKey = new byte[keyLength];

    public static ByteStream decrypt(short id, ByteStream stream) {
        switch (id) {
            case 20100:
            case 20103: {
                return stream;
            }
            default: {
                return stream;
            }
        }
    }

    public static byte[] encrypt(short id, ByteStream stream) {
        byte[] encrypted = null;
        byte[] decrypted = null;

        switch (id) {
            case 10100: {
                return encrypted = stream.array();
            }
            case 10101: {
                Blake2b blake2b = Blake2b.Digest.newInstance();
                blake2b.update(keyPair.pk);
                blake2b.update(Configuration.Keys.PUBLIC_SERVER_KEY);
                byte[] tmpNonce = blake2b.digest();

                new Random().nextBytes(_10101_Nonce);

                decrypted = Hex.concatBytes(Client.info.getSessionKey(), _10101_Nonce, stream.array());
                byte[] tmp = CustomNaCl.createPublicBox(decrypted, tmpNonce, keyPair.sk, Configuration.Keys.PUBLIC_SERVER_KEY);
                encrypted = Hex.concatBytes(keyPair.pk, tmp);

                return encrypted;
            }
            default: {
                return encrypted;
            }
        }
    }
}
