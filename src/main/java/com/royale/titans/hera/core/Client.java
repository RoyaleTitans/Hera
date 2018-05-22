package com.royale.titans.hera.core;

import com.neilalexander.jnacl.crypto.curve25519xsalsa20poly1305;
import com.royale.titans.hera.Configuration;
import com.royale.titans.hera.crypto.sodium.ClientCrypto;
import com.royale.titans.hera.crypto.sodium.ServerCrypto;
import com.royale.titans.hera.logic.Account;
import com.royale.titans.hera.logic.enums.ClientState;
import com.royale.titans.hera.protocol.MessageManager;
import com.royale.titans.hera.protocol.PiranhaMessage;
import com.royale.titans.hera.protocol.messages.client.ClientHelloMessage;
import com.royale.titans.hera.utils.Debugger;
import com.royale.titans.hera.utils.binary.ByteStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client extends Thread {

    private static Socket socket;

    public static ClientInfo info;

    public static void connect() {
        Client.info = new ClientInfo();

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("game.clashroyaleapp.com", 9339));

            Client.info.setState(ClientState.CONNECTED);

            if (Client.info.getState() == ClientState.CONNECTED)
                send(new ClientHelloMessage());

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(PiranhaMessage message) {
        try {
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

            byte[] encrypted = Client.info.getClientCrypto().encryptPacket(message.getId(), message.encode().array());
            byte[] buffer = message.toBytes(encrypted);

            writer.write(buffer);

            Debugger.info("Sent " + message.getClass().getSimpleName() + " (" + message.getId() + ")");

            receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void receive() {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());

            short id = (short) ((short) (in.read() & 0xFF) << 8 | (in.read() & 0xFF));
            int length = (in.read() & 0xFF) << 16 | (in.read() & 0xFF) << 8 | (in.read() & 0xFF);

            byte[] temp = new byte[length];
            in.readShort();
            in.readFully(temp);

            ByteStream stream = ByteStream.wrap(temp);

            MessageManager.receiveMessage(id, stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ClientInfo {
        private ClientCrypto mClientCrypto;
        private ServerCrypto mServerCrypto;

        private byte[] mSessionKey;

        private final Account mAccount;

        private ClientState mState;

        public static String androidId = "";

        public static String region = "en-US";

        public static String openUDID = "";
        public static String model = "Hera";
        public static String osVersion = "1.0";
        public static String macAddress = "";
        public static String advertiseId = "";
        public static String vendorId = "";

        public static boolean android = true;
        public static boolean advertising = false;

        public ClientInfo() {
            byte[] serverKey = Configuration.Keys.PUBLIC_SERVER_KEY, privateKey = new byte[32];
            curve25519xsalsa20poly1305.crypto_box_keypair(serverKey, privateKey);

            mClientCrypto = new ClientCrypto(serverKey);
            mServerCrypto = new ServerCrypto(privateKey, serverKey);

            mAccount = new Account();
        }

        public ClientState getState() {
            return mState;
        }

        public void setState(ClientState state) {
            mState = state;
        }

        public byte[] getSessionKey() {
            return mSessionKey;
        }

        public void setSessionKey(byte[] sessionKey) {
            mSessionKey = sessionKey;
        }

        public ClientCrypto getClientCrypto() {
            return mClientCrypto;
        }

        public ServerCrypto getServerCrypto() {
            return mServerCrypto;
        }

        public Account getAccount() {
            return mAccount;
        }
    }
}
