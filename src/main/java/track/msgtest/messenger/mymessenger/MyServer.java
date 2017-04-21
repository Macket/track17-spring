package track.msgtest.messenger.mymessenger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.LinkedList;

public class MyServer {
    private static final int BUFSIZE = 100;
    private static LinkedList<OutputStream> outs = new LinkedList<>();
    private static BlockingQueue<byte[]> recieveBufs = new BlockingQueue<>();

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Parameter: <Port>");
        }
        LinkedList<Integer> list = new  LinkedList<>();

        int servPort = Integer.parseInt(args[0]);

        ServerSocket serverSocket = new ServerSocket(servPort);

        while (true) {
            Socket clntSock = serverSocket.accept();
            outs.addLast(clntSock.getOutputStream());

            Thread inThread = new Thread() {
                @Override
                public void run() {
                    try {
                        int recvMsgSize;
                        byte[] recieveBuf = new byte[BUFSIZE];

                        SocketAddress clientAddress = clntSock.getRemoteSocketAddress();
                        System.out.println("Handling client at " + clientAddress);

                        InputStream in = clntSock.getInputStream();

                        while ((recvMsgSize = in.read(recieveBuf)) != -1) {
                            OutThread outThread = new OutThread(recvMsgSize, recieveBuf, outs);
                            outThread.start();
                        }
                        clntSock.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            };

            inThread.start();
        }
    }
}