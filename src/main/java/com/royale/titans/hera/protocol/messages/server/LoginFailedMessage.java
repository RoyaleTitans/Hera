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
    public VInt mErrorCode;
    public String mFingerprint;

    public String mAssetsUrl;
    public String mPatchingHost;

    public LoginFailedMessage(ByteStream stream) {
        super(stream);

        mErrorCode = stream.readVInt();
        mFingerprint = stream.readString();

        stream.readString();

        mAssetsUrl = stream.readString();
        mPatchingHost = stream.readString();
    }

    @Override
    public void process() {
        switch (mErrorCode.getValue()) {
            case 7: {
                try {
                    FileOutputStream writer = new FileOutputStream(new File("fingerprint.json"));
                    writer.write(mFingerprint.getBytes());
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
                Debugger.info("Client is outdated.");
                break;
            }
            case 10: {
                Debugger.info("Server is in maintenance.");
                break;
            }
            default: {
                break;
            }
        }

        Client.send(new ClientHelloMessage());
    }
}
