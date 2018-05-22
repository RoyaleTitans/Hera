package com.royale.titans.hera.protocol.messages.server;

import com.royale.titans.hera.core.Client;
import com.royale.titans.hera.protocol.PiranhaMessage;
import com.royale.titans.hera.protocol.messages.client.LoginMessage;
import com.royale.titans.hera.utils.Debugger;
import com.royale.titans.hera.utils.binary.ByteStream;
import com.royale.titans.hera.utils.binary.Hex;

public class ServerHelloMessage extends PiranhaMessage {
    private byte[] mSessionKey;

    public ServerHelloMessage(ByteStream stream) {
        super(stream);

        mSessionKey = stream.readArray();
    }

    @Override
    public void process() {
        Client.info.setSessionKey(mSessionKey);

        Debugger.debug("Session Key : " + Hex.toString(mSessionKey) + "\n");

        Client.send(new LoginMessage());
    }
}
