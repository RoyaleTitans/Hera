package com.royale.titans.hera.crypto.sodium;

import com.royale.titans.hera.protocol.PiranhaMessage;
import com.royale.titans.hera.utils.binary.ByteStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ClientCrypto extends Crypto {
    public ClientCrypto(byte[] serverKey) {
        super();

        TweetNaCl.crypto_sign_keypair(privateKey, clientKey, false);
        this.serverKey = serverKey;

        sharedKey = Curve25519.scalarMult(privateKey, serverKey);
        sharedKey = Salsa.HSalsa20(new byte[16], sharedKey, Salsa.SIGMA);

        encryptNonce = new Nonce();
    }

    @Override
    public ByteStream decryptPacket(short id, byte[] message) {
        ByteStream stream = null;

        switch (id) {
            case 20100:
            case 20103:
                return ByteStream.wrap(message);
            case 22194:
                Nonce nonce = new Nonce(clientKey, serverKey, encryptNonce.getBytes());
                stream = decrypt(message, nonce);

                if (message != null) {
                    try {
                        decryptNonce = new Nonce(Arrays.copyOfRange(message, 0, 24));
                        encryptNonce = new Nonce(Arrays.copyOfRange(message, 0, 24));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    sharedKey = Arrays.copyOfRange(message, 24, 56);

                    message = Arrays.copyOfRange(message, 56, message.length);
                }
                return stream;
            default:
                return decrypt(message);
        }
    }

    @Override
    public ByteStream encryptPacket(PiranhaMessage message) {
        switch (message.id) {
            case 10100:
                return message.encode();
            case 10101:
                Nonce nonce = new Nonce(clientKey, serverKey);
                ByteArrayOutputStream toEncrypt = new ByteArrayOutputStream();

                try {
                    toEncrypt.write(sessionKey);
                    toEncrypt.write(encryptNonce.getBytes());
                    toEncrypt.write(message.encode().array());
                } catch (IOException ignored) {
                }

                ByteArrayOutputStream encrypted = new ByteArrayOutputStream();

                try {
                    encrypted.write(clientKey);
                    encrypted.write(encrypt(toEncrypt.toByteArray(), nonce).array());
                } catch (IOException ignored) {
                }

                return ByteStream.wrap(encrypted.toByteArray());
            default:
                return encrypt(message.encode().array());
        }
    }
}
