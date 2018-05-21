package com.royale.titans.hera.protocol.messages.server;

import com.royale.titans.hera.protocol.PiranhaMessage;
import com.royale.titans.hera.utils.binary.ByteStream;

public class KeepAliveServerMessage extends PiranhaMessage {

    public KeepAliveServerMessage(ByteStream stream) {
        super(stream);
    }
}
