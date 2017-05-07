package track.msgtest.messenger.net;

import com.sun.deploy.ref.AppModel;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.Type;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by ivan on 06.05.17.
 */
public class InThread implements Runnable {
    Socket clntSock;

    public InThread(Socket clntSock) {
        this.clntSock = clntSock;
    }

    @Override
    public void run() {
        try {
            SocketAddress clientAddress = clntSock.getRemoteSocketAddress();
            System.out.println("Handling client at " + clientAddress);

            ObjectInputStream ois = new ObjectInputStream(clntSock.getInputStream());

            while (true) {
                try {
                    Message msg = (Message) ois.readObject();
                    Type type = msg.getType();
                    switch (type) {
                        case MSG_TEXT:
                            MessengerServer.recieveMsgs.put(msg);
                            break;
                        case MSG_LOGIN:
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
