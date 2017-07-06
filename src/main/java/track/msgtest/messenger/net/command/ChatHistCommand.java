package track.msgtest.messenger.net.command;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.TextMessage;
import track.msgtest.messenger.net.InThread;

import java.net.Socket;
import java.util.List;

/**
 * Created by ivan on 06.07.17.
 */
public class ChatHistCommand extends Command {

    public void execute(Message msg, InThread inThread) {
        User user = inThread.getUser();
        Socket clntSock = inThread.getClntSock();
        List<TextMessage> hist = messageStore.getMessagesFromChat(user.getCurrentChatId());
        for (TextMessage textMsg : hist) {
            try {
                sendMessage(textMsg, user, clntSock);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
