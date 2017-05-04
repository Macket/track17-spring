package track.msgtest.messenger.mymessenger;

/**
 * Created by ivan on 18.04.17.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class MyClient {
    private static final int BUFSIZE = 2048;

    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args.length > 2) {
            throw new IllegalArgumentException("Parameters: <Server> <word> [<Port>]");
        }

        String server = args[0];

        int servPort = (args.length == 2) ? Integer.parseInt(args[1]) : 7;

        Socket socket = new Socket(server, servPort);
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        Thread outThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    // Считываем сообщение с консоли
                    Scanner scanner = new Scanner(System.in);
                    byte[] data = scanner.nextLine().getBytes();
                    try {
                        out.write(data);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        outThread.start();

        byte[] data = new byte[BUFSIZE];
        int bytesRcvd;

        while ((bytesRcvd = in.read(data)) != -1) {
            System.out.println("Received:" + new String(data).trim());
            data = new byte[BUFSIZE];
        }
        //socket.close();
    }
}
