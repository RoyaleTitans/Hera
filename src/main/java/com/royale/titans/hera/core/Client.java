package com.royale.titans.hera.core;

import com.royale.titans.hera.Configuration;
import com.royale.titans.hera.crypto.sodium.ClientCrypto;
import com.royale.titans.hera.logic.Player;
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

            Client.info.state = ClientState.CONNECTED;

            if (Client.info.state == ClientState.CONNECTED)
                send(new ClientHelloMessage());

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(PiranhaMessage message) {
        try {
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

            byte[] encrypted = info.crypto.encryptPacket(message).array();
            byte[] buffer = message.toBytes(encrypted);

            writer.write(buffer);

            Debugger.info("Sent " + message.getClass().getSimpleName() + " (" + message.id + ")");

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
        public ClientCrypto crypto;

        public String androidId = "0d2c46b3-361b-4a76-aee0-f22032f1ce01";

        public String region = "en-US";

        public String openUDID = "4699c1d58f3532c1";
        public String model = "ClashRoyale.Client";
        public String osVersion = "1.0";
        public String macAddress = "e859e074-c4cb-1602-8227-c7de1ec71abc";
        public String advertiseId = "";
        public String vendorId = "";

        public boolean android = true;
        public boolean advertising = false;

        public Player player;

        public ClientState state;

        public ClientInfo() {
            this.player = new Player();

            this.crypto = new ClientCrypto(Configuration.SERVER_KEY);
        }
    }
}
