package track.msgtest.messenger.net.command;

import org.omg.CORBA.UserException;
import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.TextMessage;
import track.msgtest.messenger.net.*;
import track.msgtest.messenger.store.DbManager;
import track.msgtest.messenger.store.MessageStore;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * Created by ivan on 06.07.17.
 */
public class TextCommand extends Command {

    public void execute(Message msg, InThread inThread) {
        User user = inThread.getUser();
        if (user.getCurrentChatId() > 0) {
            TextMessage textMessage = (TextMessage) msg;
            textMessage.setSenderId(user.getId());
            textMessage.setSenderName(user.getName());
            textMessage.setChatId(user.getCurrentChatId());
            clntSocketMap.forEach((reciever, socket) -> {
                try {
                    if (user.getCurrentChatId() > 0) {
                        messageStore.addMessage(textMessage);
                        if (user.getCurrentChatId() == user.getCurrentChatId()) {
                            sendMessage(textMessage, user, socket);
                        }
                    }
                } catch (IOException ioex) {
                    close(user, socket);
                    System.out.println("Failed to send message to " + socket);
                } catch (ProtocolException pex) {
                    pex.printStackTrace();
                }
            });
        }
    }

    public void close(User user, Socket socket) {
        try {
            socket.close();
            clntSocketMap.remove(user, socket);
        } catch (IOException ex) {

        }
    }
}
