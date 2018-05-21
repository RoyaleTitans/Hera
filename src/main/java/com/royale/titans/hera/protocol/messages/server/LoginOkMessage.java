package com.royale.titans.hera.protocol.messages.server;

import com.royale.titans.hera.protocol.PiranhaMessage;
import com.royale.titans.hera.utils.binary.ByteStream;

public class LoginOkMessage extends PiranhaMessage {

    public LoginOkMessage(ByteStream stream) {
        super(stream);
    }

    @Override
    public void process() {
    }
}
