package com.royale.titans.hera.protocol.messages.client;

import com.royale.titans.hera.core.Client;
import com.royale.titans.hera.core.Resources;
import com.royale.titans.hera.protocol.PiranhaMessage;
import com.royale.titans.hera.utils.binary.ByteStream;

public class LoginMessage extends PiranhaMessage {

    private static final short ID = 10101;

    public LoginMessage() {
        super(ID);
    }

    @Override
    public ByteStream encode() {
        ByteStream stream = new ByteStream();

        stream.writeLong(Client.info.getAccount().getAccountId());
        stream.writeString(Client.info.getAccount().getToken());

        stream.writeVInt(Integer.parseInt(Resources.version[0]));
        stream.writeVInt(Integer.parseInt(Resources.version[2]));
        stream.writeVInt(Integer.parseInt(Resources.version[1]));
        stream.writeString(Resources.fingerprint.getHash());

        stream.writeInt(0);

        stream.writeString(Client.info.openUDID);
        stream.writeString(Client.info.macAddress);
        stream.writeString(Client.info.model);
        stream.writeString(Client.info.advertiseId);
        stream.writeString(Client.info.osVersion);

        stream.write((byte) 1);

        stream.writeInt(0);

        stream.writeString(Client.info.androidId);
        stream.writeString(Client.info.region);

        stream.writeString("");
        stream.write((byte) 1);
        stream.writeInt(0);
        stream.write((byte) 2);
        stream.writeInt(0);
        stream.writeInt(0);
        stream.writeInt(0);
        stream.writeInt(0);
        stream.write((byte) 0);

        return stream.obtain();
    }
}
