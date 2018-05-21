package com.royale.titans.hera.crypto.sodium;

import com.royale.titans.hera.protocol.PiranhaMessage;
import com.royale.titans.hera.utils.binary.ByteStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ClientCrypto extends Crypto {
    public ClientCrypto(byte[] serverKey) {
        super();

        TweetNaCl.crypto_sign_keypair(mPrivateKey, mClientKey, false);
        mServerKey = serverKey;

        mSharedKey = Curve25519.scalarMult(mPrivateKey, serverKey);
        mSharedKey = Salsa.HSalsa20(new byte[16], mSharedKey, Salsa.SIGMA);

        mEncryptNonce = new Nonce();
    }

    @Override
    public ByteStream decryptPacket(short id, byte[] message) {
        ByteStream stream = null;

        switch (id) {
            case 20100:
            case 20103:
                return ByteStream.wrap(message);
            case 22194:
                Nonce nonce = new Nonce(mClientKey, mServerKey, mEncryptNonce.getBytes());
                stream = decrypt(message, nonce);

                if (message != null) {
                    try {
                        mDecryptNonce = new Nonce(Arrays.copyOfRange(message, 0, 24));
                        mEncryptNonce = new Nonce(Arrays.copyOfRange(message, 0, 24));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mSharedKey = Arrays.copyOfRange(message, 24, 56);

                    message = Arrays.copyOfRange(message, 56, message.length);
                }
                return stream;
            default:
                return decrypt(message);
        }
    }

    @Override
    public ByteStream encryptPacket(PiranhaMessage message) {
        switch (message.getId()) {
            case 10100:
                return message.encode();
            case 10101:
                Nonce nonce = new Nonce(mClientKey, mServerKey);
                ByteArrayOutputStream toEncrypt = new ByteArrayOutputStream();

                try {
                    toEncrypt.write(mSessionKey);
                    toEncrypt.write(mEncryptNonce.getBytes());
                    toEncrypt.write(message.encode().array());
                } catch (IOException ignored) {
                }

                ByteArrayOutputStream encrypted = new ByteArrayOutputStream();

                try {
                    encrypted.write(mClientKey);
                    encrypted.write(encrypt(toEncrypt.toByteArray(), nonce).array());
                } catch (IOException ignored) {
                }

                return ByteStream.wrap(encrypted.toByteArray());
            default:
                return encrypt(message.encode().array());
        }
    }
}
