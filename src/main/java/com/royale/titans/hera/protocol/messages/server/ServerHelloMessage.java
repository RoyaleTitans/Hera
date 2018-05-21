package com.royale.titans.hera.protocol.messages.server;

import com.royale.titans.hera.core.Client;
import com.royale.titans.hera.protocol.PiranhaMessage;
import com.royale.titans.hera.protocol.messages.client.LoginMessage;
import com.royale.titans.hera.utils.Debugger;
import com.royale.titans.hera.utils.binary.ByteStream;
import com.royale.titans.hera.utils.binary.Hex;

public class ServerHelloMessage extends PiranhaMessage {

    public byte[] sessionKey;

    public ServerHelloMessage(ByteStream stream) {
        super(stream);

        this.sessionKey = stream.readArray();
    }

    @Override
    public void process() {
        Client.info.crypto.setSessionKey(this.sessionKey);

        Debugger.debug("Session Key : " + Hex.toString(this.sessionKey) + "\n");

        Client.send(new LoginMessage());
    }
}
