package track.msgtest.messenger.mymessenger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyServer {
    public static final int BUFSIZE = 2048;
    private static final int N_THREADS = 2;
    private static LinkedList<OutputStream> outs = new LinkedList<>();
    public static BlockingQueue<Message> recieveMsgs = new BlockingQueue<>();
    //private static LinkedList<Thread> inThreads = new  LinkedList<>();
    private static ExecutorService inThreadPool = Executors.newFixedThreadPool(N_THREADS);

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Parameter: <Port>");
        }

        int servPort = Integer.parseInt(args[0]);

        ServerSocket serverSocket = new ServerSocket(servPort);

        Thread outThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Message sendMsg = recieveMsgs.take();
                        for (OutputStream out : outs) {
                            out.write(sendMsg.getRecieveBuf(), 0, sendMsg.getRecvMsgSize());
                            out.flush();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        outThread.start();

        while (true) {
            Socket clntSock = serverSocket.accept();
            outs.addLast(clntSock.getOutputStream());

            inThreadPool.submit(new InThread(clntSock));
        }
    }
}