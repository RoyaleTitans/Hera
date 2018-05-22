package com.royale.titans.hera.crypto.sodium;

import com.royale.titans.hera.protocol.PiranhaMessage;

public class ClientCrypto extends Crypto {

    public byte[] encrypt(PiranhaMessage message) {
        switch (message.getId()) {
            case 10100: {
                return message.encode().array();
            }
            default: {
                return null;
            }
        }
    }

    public byte[] decrypt(short id, byte[] data) {
        switch (id) {
            case 20100:
            case 20103: {
                return data;
            }
            default: {
                return null;
            }
        }
    }
}
