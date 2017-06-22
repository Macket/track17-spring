package track.msgtest.messenger.net;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.LoginMessage;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.store.DbManager;

import java.io.*;
import java.net.Socket;
import java.util.Map;

/**
 * Created by ivan on 06.05.17.
 */
public class InThread implements Runnable {
    private static Map<User, OutputStream> outputStreamMap = MessengerServer.getOutputStreamMap();
    private Socket clntSock;
    private InputStream ois;
    private OutputStream oos;
    private Protocol protocol = new BinaryProtocol();

    public InThread(Socket clntSock) throws IOException {
        this.clntSock = clntSock;
        ois = clntSock.getInputStream();
        oos = clntSock.getOutputStream();
    }

    @Override
    public void run() {

        try {

            DbManager dbManager = new DbManager();

            while (true) {
                byte[] buf = new byte[2048];
                ois.read(buf);
                Message msg = protocol.decode(buf);
                Type type = msg.getType();
                switch (type) {
                    case MSG_TEXT:
                        outputStreamMap.forEach((user, out) -> {
                            try {
                                out.write(protocol.encode(msg));
                                out.flush();
                            } catch (Exception e) {
                                outputStreamMap.remove(user);
                                System.out.println("Failed to send message to " + out);
                            }
                        });
                        //MessengerServer.recieveMsgs.put(msg);
                        break;
                    case MSG_LOGIN:
                        LoginMessage loginMessage = (LoginMessage) msg;
                        User newUser = dbManager.getUser(loginMessage.getName(), loginMessage.getPass());
                        if (newUser == null) {
                            newUser = dbManager.insertUser(loginMessage.getName(), loginMessage.getPass());
                        }
                        outputStreamMap.putIfAbsent(newUser, oos);
                        break;
                    default:
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

