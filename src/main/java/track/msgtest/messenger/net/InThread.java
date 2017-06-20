package track.msgtest.messenger.net;

import com.sun.deploy.ref.AppModel;
import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.LoginMessage;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.Type;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivan on 06.05.17.
 */
public class InThread implements Runnable {
    private static Map<User, OutputStream> outputStreamMap = MessengerServer.getOutputStreamMap();
    private InputStream ois;
    private OutputStream oos;
    private Protocol protocol = new BinaryProtocol();

    public InThread(InputStream ois, OutputStream oos) {
        this.ois = ois;
        this.oos = oos;
    }

    @Override
    public void run() {

        while (true) {
            try {
                byte[] buf = new byte[2048];
                ois.read(buf);
                Message msg = protocol.decode(buf);
                Type type = msg.getType();
                switch (type) {
                    case MSG_TEXT:
                        outputStreamMap.values().forEach(out -> {
                            try {
                                out.write(protocol.encode(msg));
                                out.flush();
                            } catch (Exception e) {
                                // delete Out
                                System.out.println("Failed to send message to " + out);
                            }
                        });
                        //MessengerServer.recieveMsgs.put(msg);
                        break;
                    case MSG_LOGIN:
                        LoginMessage loginMessage = (LoginMessage) msg;
                        User newUser = new User(loginMessage.getName(), loginMessage.getPass());
                        outputStreamMap.putIfAbsent(newUser, oos);
                        break;
                    default:

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}
