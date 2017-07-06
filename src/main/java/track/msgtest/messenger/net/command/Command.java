package track.msgtest.messenger.net.command;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.net.*;
import track.msgtest.messenger.store.ChatStore;
import track.msgtest.messenger.store.DbManager;
import track.msgtest.messenger.store.MessageStore;
import track.msgtest.messenger.store.UserStore;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * Created by ivan on 06.07.17.
 */
public abstract class Command {
    private Protocol protocol = new BinaryProtocol();

    protected ChatStore chatStore = new DbManager();
    protected UserStore userStore = new DbManager();
    protected MessageStore messageStore = new DbManager();

    protected static Map<User, Socket> clntSocketMap = MessengerServer.getClntSocketMap();

    public abstract void execute(Message msg, InThread inThread);

    public void sendMessage(Message msg, User user, Socket socket) throws ProtocolException, IOException {
        OutputStream out = socket.getOutputStream();
        out.write(protocol.encode(msg));
        out.flush();
    }
}
