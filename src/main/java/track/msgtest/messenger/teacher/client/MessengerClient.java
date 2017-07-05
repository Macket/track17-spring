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
import track.msgtest.messenger.net.StringProtocol;


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

        //ObjectInputStream objectInputStream = new ObjectInputStream(in);
        //oos = new ObjectOutputStream(out);

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
        log.info("Message received: {}", msg);
    }

    /**
     * Обрабатывает входящую строку, полученную с консоли
     * Формат строки можно посмотреть в вики проекта
     */
    public void processInput(String line) throws IOException, ProtocolException {
        String[] tokens = line.split(" ", 2);
        log.info("Tokens: {}", Arrays.toString(tokens));
        String cmdType = tokens[0];
        switch (cmdType) {

            case "/login":
                String[] nameAndPass = tokens[1].split(" ");
                if (nameAndPass.length != 2) {
                    System.out.println("Ошибка входа");
                    break;
                } else {
                    LoginMessage sendLoginMessage = new LoginMessage(nameAndPass[0], nameAndPass[1]);
                    send(sendLoginMessage);
                }
                break;

            case "/chat_create":
                ChatCreateMessage chatCreateMessage = new ChatCreateMessage(tokens[1]);
                chatCreateMessage.setType(Type.MSG_CHAT_CREATE);
                send(chatCreateMessage);
                break;

            case "/chat_join":
                ChatJoinMessage chatJoinMessage = new ChatJoinMessage(tokens[1]);
                chatJoinMessage.setType(Type.MSG_CHAT_JOIN);
                chatJoinMessage.setName(tokens[1]);
                send(chatJoinMessage);
                break;

            case "/chat_exit":
                ChatExitMessage chatExitMessage = new ChatExitMessage();
                chatExitMessage.setType(Type.MSG_CHAT_EXIT);
                send(chatExitMessage);
                break;

            case "/chat_list":
                ChatListMessage chatListMessage = new ChatListMessage();
                chatListMessage.setType(Type.MSG_CHAT_LIST);
                send(chatListMessage);
                break;

            case "/chat_hist":
                ChatHistMessage chatHistMessage = new ChatHistMessage();
                chatHistMessage.setType(Type.MSG_CHAT_HIST);
                send(chatHistMessage);
                break;

            case "/text":
                // FIXME: пример реализации для простого текстового сообщения
                TextMessage sendTextMessage = new TextMessage();
                sendTextMessage.setType(Type.MSG_TEXT);
                sendTextMessage.setText(tokens[1]);
                send(sendTextMessage);
                break;
            // TODO: implement another types from wiki

            default:
                log.error("Invalid input: " + line);
        }
    }

    /**
     * Отправка сообщения в сокет клиент -> сервер
     */
    public void send(Message msg) throws IOException, ProtocolException {
        log.info(msg.toString());
        out.write(protocol.encode(msg));

        out.flush(); // принудительно проталкиваем буфер с данными
    }

    public static void main(String[] args) throws Exception {

        MessengerClient client = new MessengerClient();
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