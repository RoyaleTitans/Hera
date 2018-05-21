package com.royale.titans.hera.protocol.messages.server;

import com.royale.titans.hera.Configuration;
import com.royale.titans.hera.core.Client;
import com.royale.titans.hera.core.Downloader;
import com.royale.titans.hera.core.Resources;
import com.royale.titans.hera.definitions.Fingerprint;
import com.royale.titans.hera.protocol.PiranhaMessage;
import com.royale.titans.hera.protocol.messages.client.ClientHelloMessage;
import com.royale.titans.hera.utils.Debugger;
import com.royale.titans.hera.utils.binary.ByteStream;
import com.royale.titans.hera.utils.binary.VInt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoginFailedMessage extends PiranhaMessage {

    public VInt errorCode;
    public String fingerprint;

    public int unk_0;
    public int unk_1;
    public int unk_2;
    public int unk_3;
    public int unk_4;
    public int unk_5;
    public int unk_6;

    public String assetsUrl;
    public String patchingHost;

    public LoginFailedMessage(ByteStream stream) {
        super(stream);

        this.errorCode = stream.readVInt();
        Debugger.info(this.errorCode.getValue());

        this.fingerprint = stream.readString();

        stream.readString();

        this.assetsUrl = stream.readString();
        this.patchingHost = stream.readString();
    }

    @Override
    public void process() {
        switch (this.errorCode.getValue()) {
            case 7: {
                // Patch

                try {
                    FileOutputStream writer = new FileOutputStream(new File("fingerprint.json"));
                    writer.write(this.fingerprint.getBytes());
                    writer.close();

                    Resources.fingerprint = Fingerprint.read();
                    Resources.version = Resources.fingerprint.getVersion().replace(".", ",").split(",");

                    if (Configuration.DOWNLOAD_MODE)
                        Downloader.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }
            case 8: {
                // Client update

                break;
            }
            default: {
                break;
            }
        }

        Client.send(new ClientHelloMessage());
    }
}
