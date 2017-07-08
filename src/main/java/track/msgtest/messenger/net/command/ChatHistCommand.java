package track.msgtest.messenger.net.command;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.TextMessage;
import track.msgtest.messenger.net.Session;

import java.net.Socket;
import java.util.List;

/**
 * Created by ivan on 06.07.17.
 */
public class ChatHistCommand extends Command {

    public void execute(Message msg, Session session) {
        User user = session .getUser();
        Socket clntSock = session .getClntSock();
        List<TextMessage> hist = messageStore.getMessagesFromChat(user.getCurrentChatId());
        for (TextMessage textMsg : hist) {
            try {
                sendMessage(textMsg, clntSock);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
