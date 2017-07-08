package track.msgtest.messenger.teacher.client;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import track.msgtest.messenger.messages.*;
import track.msgtest.messenger.net.BinaryProtocol;
import track.msgtest.messenger.net.Protocol;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.teacher.client.command.Commands;


/**
 *
 */
public class MessengerClient {


    /**
     * Механизм логирования позволяет более гибко управлять записью данных в лог (консоль, файл и тд)
     * */
    static Logger log = LoggerFactory.getLogger(MessengerClient.class);

    /**
     * Протокол, хост и порт инициализируются из конфига
     *
     * */
    private Protocol protocol;
    private int port;
    private String host;

    /**
     * Комманды, которые вызываются по ключам
     *
     * */
    private static Commands commands;

    /**
     * Текущие пользователь и чат
     *
     * */
    private String curChat;
    private String curUser;

    /**
     * С каждым сокетом связано 2 канала in/out
     */
    private InputStream in;
    private OutputStream out;

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void initSocket() throws IOException {
        Socket socket = new Socket(host, port);
        in = socket.getInputStream();
        out = socket.getOutputStream();
        curChat = "none";
        curUser = "none";

        /*
      Тред "слушает" сокет на наличие входящих сообщений от сервера
     */
        Thread socketListenerThread = new Thread(() -> {
            log.info("Starting listener thread...");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    byte[] buf = new byte[2048];
                    in.read(buf);
                    // Здесь поток блокируется на ожидании данных
                    Message msg = protocol.decode(buf);
                    onMessage(msg);
                } catch (Exception e) {
                    log.error("Failed to process connection: {}", e);
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });

        socketListenerThread.start();
    }

    /**
     * Реагируем на входящее сообщение
     */
    public void onMessage(Message msg) {
        Type type = msg.getType();
        switch (type) {
            case MSG_TEXT:
                TextMessage textMessage = (TextMessage) msg;
                System.out.println("(" + textMessage.getTimestamp().toString() + ") " +
                        textMessage.getSenderName() + ": " + textMessage.getText());
                break;
            case MSG_LOGIN_RESULT:
                LoginResultMessage loginResultMessage = (LoginResultMessage) msg;
                if (loginResultMessage.getStatus() == Status.OK) {
                    curUser = loginResultMessage.getSenderName();
                    System.out.println("Вы вошли под именем " + curUser + "\n$");
                } else {
                    System.out.println("Ошибка входа");
                }
                break;
            case MSG_CURRENT_CHAT:
                CurrentChatMessage currentChatMessage = (CurrentChatMessage) msg;
                curChat = currentChatMessage.getChatName();
                break;
            default:

        }
    }

    /**
     * Обрабатывает входящую строку, полученную с консоли
     * Формат строки можно посмотреть в вики проекта
     */
    public void processInput(String line) throws IOException, ProtocolException {
        String[] tokens = line.split(" ", 2);
        if (tokens.length == 1) {
            //log.info("Tokens: {}", Arrays.toString(tokens));
            try {
                commands.getCommand(tokens[0]).execute("", out);
            } catch (NullPointerException ex) {

            }
        } else {
            //log.info("Tokens: {}", Arrays.toString(tokens));
            try {
                commands.getCommand(tokens[0]).execute(tokens[1], out);
            } catch (NullPointerException ex) {

            }
        }
    }


    public static void main(String[] args) throws Exception {

        MessengerClient client = new MessengerClient();
        commands = new Commands();
        client.setHost("localhost");
        client.setPort(8000);
        client.setProtocol(new BinaryProtocol());

        try {
            client.initSocket();

            // Цикл чтения с консоли
            Scanner scanner = new Scanner(System.in);
            System.out.println("$");
            while (true) {
                String input = scanner.nextLine();
                if ("q".equals(input)) {

                    return;
                }
                try {
                    client.processInput(input);
                } catch (ProtocolException | IOException e) {
                    log.error("Failed to process user input", e);
                }
                System.out.println("$ (Пользователь: " + client.curUser + ", Чат: " + client.curChat + ")");
            }
        } catch (Exception e) {
            log.error("Application failed.", e);
        } finally {
            if (client != null) {
                // TODO
//                client.close();
            }
        }
    }
}