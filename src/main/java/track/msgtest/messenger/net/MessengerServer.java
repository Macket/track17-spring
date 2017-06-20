package track.msgtest.messenger.net;

import org.omg.CORBA.Object;
import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.blockingqueue.BlockingQueue;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class MessengerServer {
    private static final int N_THREADS = 10;
    //public static LinkedList<ObjectOutputStream> ooss = new LinkedList<>();
    public static BlockingQueue<Message> recieveMsgs = new BlockingQueue<>();
    private static Map<User, OutputStream> outputStreamMap = new ConcurrentHashMap<>();
    private static ExecutorService inThreadPool = Executors.newFixedThreadPool(N_THREADS);

    public static Map<User, OutputStream> getOutputStreamMap() {
        return outputStreamMap;
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Parameter: <Port>");
        }

        int servPort = Integer.parseInt(args[0]);

        ServerSocket serverSocket = new ServerSocket(servPort);

//        Thread outThread = new Thread() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Message sendMsg = recieveMsgs.take();
//                        for (Map.Entry<User, OutputStream> entry : outputStreamMap.entrySet()) {
//                            try {
//                                ObjectOutputStream oos = entry.getValue();
//                                if (oos == null) {
//                                    outputStreamMap.remove(entry.getKey());
//                                    continue;
//                                }
//                                oos.writeObject(sendMsg);
//                                oos.flush();
//                            } catch (IOException ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                    } catch (InterruptedException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        };
//
//        outThread.start();

        while (true) {
            Socket clntSock = serverSocket.accept();

            SocketAddress clientAddress = clntSock.getRemoteSocketAddress();
            System.out.println("Handling client at " + clientAddress);

            InputStream ois = clntSock.getInputStream();
            OutputStream oos = clntSock.getOutputStream();
            //ooss.addLast(new ObjectOutputStream(clntSock.getOutputStream()));

            inThreadPool.submit(new InThread(ois, oos));
        }
    }
}
