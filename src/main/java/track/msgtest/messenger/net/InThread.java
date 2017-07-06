package track.msgtest.messenger.net;

import track.msgtest.messenger.User;
import track.msgtest.messenger.net.command.Commands;
import track.msgtest.messenger.messages.*;

import java.io.*;
import java.net.Socket;
import java.util.Map;

/**
 * Created by ivan on 06.05.17.
 */
public class InThread implements Runnable {
    private static Map<User, Socket> clntSocketMap = MessengerServer.getClntSocketMap();
    private Socket clntSock;
    private User user;
    private InputStream is;
    private Protocol protocol;
    private static Commands commands;

    public InThread(Socket clntSock) throws IOException {
        this.clntSock = clntSock;
        user = null;
        is = clntSock.getInputStream();
        protocol = new BinaryProtocol();
        commands = new Commands();

    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setClntSock(Socket clntSock) {
        this.clntSock = clntSock;
    }

    public Socket getClntSock() {
        return clntSock;
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] buf = new byte[2048];
                is.read(buf);
                Message msg = protocol.decode(buf);
                onMessage(msg, this);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onMessage(Message msg, InThread inThread) {
        Type type = msg.getType();
        commands.getCommand(type).execute(msg, inThread);
    }
}

