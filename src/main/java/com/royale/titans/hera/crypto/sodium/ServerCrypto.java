package com.royale.titans.hera.crypto.sodium;

import com.neilalexander.jnacl.crypto.curve25519xsalsa20poly1305;
import com.royale.titans.hera.utils.binary.Hex;

import java.util.Arrays;

public class ServerCrypto extends Crypto {
    public ServerCrypto(byte[] privateKey, byte[] serverKey) {
        super(serverKey);
        this.privateKey = privateKey;
        setEncryptNonce(new Nonce());
    }

    public byte[] decryptPacketLogin(byte[] message, ClientCrypto clientCrypto) {
        this.clientKey = Arrays.copyOf(message, 32);
        this.sharedKey = new byte[32];
        curve25519xsalsa20poly1305.crypto_box_beforenm(this.sharedKey, this.clientKey, this.privateKey);
        Nonce nonce = new Nonce(clientKey, serverKey);
        byte[] decrypted = decrypt(Arrays.copyOfRange(message, 32, message.length), nonce);
        this.sessionKey = Arrays.copyOfRange(decrypted, 0, 24);
        this.decryptNonce = new Nonce(Arrays.copyOfRange(decrypted, 24, 48));
        if (clientCrypto != null)
            clientCrypto.encryptNonce = new Nonce(Arrays.copyOfRange(decrypted, 24, 48));
        state++;
        return Arrays.copyOfRange(decrypted, 48, decrypted.length);
    }

    public byte[] decryptPacket(byte[] message) {
        return decrypt(message, null);
    }

    public byte[] encryptPacketLoginOK(byte[] message, ClientCrypto clientCrypto) {
        Nonce nonce = new Nonce(clientKey, serverKey, decryptNonce.getBytes());
        sharedKey = clientCrypto.sharedKey;
        state++;
        return encrypt(Hex.concatBytes(encryptNonce.getBytes(), sharedKey, message), nonce);
    }

    public byte[] encryptPacket(byte[] message) {
        return encrypt(message, null);
    }
}
