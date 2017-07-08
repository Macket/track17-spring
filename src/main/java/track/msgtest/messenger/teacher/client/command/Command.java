package track.msgtest.messenger.teacher.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.net.BinaryProtocol;
import track.msgtest.messenger.net.Protocol;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.teacher.client.MessengerClient;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ivan on 06.07.17.
 */
public abstract class Command {
    static Logger log = LoggerFactory.getLogger(MessengerClient.class);
    private Protocol protocol = new BinaryProtocol();

    public abstract void execute(String inputText, OutputStream out) throws IOException, ProtocolException;

    /**
     * Отправка сообщения в сокет клиент -> сервер
     */
    public void sendMessage(Message msg, OutputStream out) throws IOException, ProtocolException {
        //log.info(msg.toString());
        out.write(protocol.encode(msg));

        out.flush(); // принудительно проталкиваем буфер с данными
    }
}
