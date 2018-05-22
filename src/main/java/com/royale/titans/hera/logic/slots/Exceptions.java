package com.royale.titans.hera.logic.slots;

import com.royale.titans.hera.logic.enums.ExceptionType;
import com.royale.titans.hera.utils.Debugger;

public class Exceptions {
    public static void throwException(String message, ExceptionType type) {
        try {
            switch (type) {
                case CRYPTO: {
                    throw new CryptoException(message);
                }
                case NONCE: {
                    throw new NonceException(message);
                }
            }
        } catch (Exception exception) {
            Debugger.error("Failed to throw a " + type + " exception!");
        }
    }

    private static class NonceException extends Exception {
        private static final long serialVersionUID = 1L;

        public NonceException(String message) {
            super(message);
        }
    }

    private static class CryptoException extends Exception {
        private static final long serialVersionUID = 1L;

        public CryptoException(String message) {
            super(message);
        }
    }
}
