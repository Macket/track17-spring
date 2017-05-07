package track.msgtest.messenger.net;

import org.omg.CORBA.Object;
import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.blockingqueue.BlockingQueue;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class MessengerServer {
    private static final int N_THREADS = 10;
    public static LinkedList<ObjectOutputStream> ooss = new LinkedList<>();
    public static BlockingQueue<Message> recieveMsgs = new BlockingQueue<>();
    private static HashMap<User, ObjectOutputStream> outputStreamMap = new HashMap<>();
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
                        for (ObjectOutputStream oos : ooss) {
                            try {
                                if (oos == null) {
                                    ooss.remove(oos);
                                    break;
                                }
                                oos.writeObject(sendMsg);
                                oos.flush();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        outThread.start();

        while (true) {
            Socket clntSock = serverSocket.accept();
            ooss.addLast(new ObjectOutputStream(clntSock.getOutputStream()));

            inThreadPool.submit(new InThread(clntSock));
        }
    }
}
