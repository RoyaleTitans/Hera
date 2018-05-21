package com.royale.titans.hera.protocol.messages.client;

import com.royale.titans.hera.protocol.PiranhaMessage;

public class KeepAliveMessage extends PiranhaMessage {

    private static final short ID = 12337;

    public KeepAliveMessage() {
        super(ID);
    }
}
