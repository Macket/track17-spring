package track.msgtest.messenger.mymessenger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.LinkedList;

/**
 * Created by ivan on 22.04.17.
 */
public class InThread implements Runnable {

    Socket clntSock;

    public InThread(Socket clntSock) {
        this.clntSock = clntSock;
    }

    @Override
    public void run() {
        try {
            int recvMsgSize;
            byte[] recieveBuf = new byte[MyServer.BUFSIZE];

            SocketAddress clientAddress = clntSock.getRemoteSocketAddress();
            System.out.println("Handling client at " + clientAddress);

            InputStream in = clntSock.getInputStream();

            while ((recvMsgSize = in.read(recieveBuf)) != -1) {
                try {
                    MyServer.recieveMsgs.put(new Message(recvMsgSize, recieveBuf));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            clntSock.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
