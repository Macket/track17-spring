package track.msgtest.messenger.net;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.*;
import track.msgtest.messenger.store.ChatStore;
import track.msgtest.messenger.store.DbManager;
import track.msgtest.messenger.store.MessageStore;
import track.msgtest.messenger.store.UserStore;

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
    private Protocol protocol = new BinaryProtocol();

    public InThread(Socket clntSock) throws IOException {
        this.clntSock = clntSock;
        is = clntSock.getInputStream();
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] buf = new byte[2048];
                is.read(buf);
                Message msg = protocol.decode(buf);
                onMessage(msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onMessage(Message msg) {
        Type type = msg.getType();
        ChatStore chatStore = new DbManager();
        UserStore userStore = new DbManager();

        switch (type) {

            case MSG_TEXT:
                if (user.getCurrentChatId() < 0) {
                    break;
                }
                TextMessage textMessage = (TextMessage) msg;
                textMessage.setSenderId(user.getId());
                textMessage.setSenderName(user.getName());
                clntSocketMap.forEach((user, socket) -> {
                    try {
                        if (user.getCurrentChatId() > 0) {
                            MessageStore messageStore = new DbManager();
                            messageStore.addMessage(textMessage);
                            if (user.getCurrentChatId() == this.user.getCurrentChatId()) {
                                sendMessage(textMessage, user, socket);
                            }
                        }
                    } catch (IOException ioex) {
                        close(user, socket);
                        System.out.println("Failed to send message to " + socket);
                    } catch (ProtocolException pex) {
                        pex.printStackTrace();
                    }
                });
                break;

            case MSG_LOGIN:
                LoginMessage loginMessage = (LoginMessage) msg;
                user = userStore.getUser(loginMessage.getName(), loginMessage.getPass());
                if (user == null) {
                    user = userStore.addUser(loginMessage.getName(), loginMessage.getPass());
                }
                if (user == null) {
                    System.out.println("Ошибка входа");
                    // отправить пользователю
                } else {
                    clntSocketMap.putIfAbsent(user, clntSock);
                }
                break;

            case MSG_CHAT_CREATE:
                ChatCreateMessage chatCreateMessage = (ChatCreateMessage) msg;
                chatCreateMessage.setSenderId(user.getId());
                long chatId = chatStore.createChat(chatCreateMessage);
                user.setCurrentChatId(chatId);
                break;

            case MSG_CHAT_JOIN:
                ChatJoinMessage chatJoinMessage = (ChatJoinMessage) msg;
                user.setCurrentChatId(chatStore.joinChat(user.getId(), chatJoinMessage.getName()));
                break;

            default:
                System.out.println("Ошибка ввода");
        }
    }

    public void sendMessage(Message msg, User user, Socket socket) throws ProtocolException, IOException {
        OutputStream out = socket.getOutputStream();
        out.write(protocol.encode(msg));
        out.flush();
    }

    public void close(User user, Socket socket) {
        try {
            socket.close();
            clntSocketMap.remove(user, socket);
        } catch (IOException ex) {

        }
    }
}

