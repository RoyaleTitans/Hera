package com.royale.titans.hera.protocol.messages.client;

import com.royale.titans.hera.Configuration;
import com.royale.titans.hera.core.Resources;
import com.royale.titans.hera.protocol.PiranhaMessage;
import com.royale.titans.hera.utils.binary.ByteStream;

import java.io.File;

public class ClientHelloMessage extends PiranhaMessage {

    private static final short ID = 10100;

    public ClientHelloMessage() {
        super(ID);
    }

    @Override
    public ByteStream encode() {
        ByteStream stream = new ByteStream();

        stream.writeInt(2);
        stream.writeInt(Configuration.KEY_VERSION);
        setVersion(stream);
        stream.writeInt(2);
        stream.writeInt(2);

        return stream.obtain();
    }

    private void setVersion(ByteStream stream) {
        if (!new File("fingerprint.json").exists()) {
            stream.writeInt(3);
            stream.writeInt(0);
            stream.writeInt(1051);
            stream.writeString("");
        } else {
            stream.writeInt(Integer.parseInt(Resources.version[0]));
            stream.writeInt(Integer.parseInt(Resources.version[2]));
            stream.writeInt(Integer.parseInt(Resources.version[1]));
            stream.writeString(Resources.fingerprint.getHash());
        }
    }
}
