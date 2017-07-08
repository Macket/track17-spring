package track.msgtest.messenger.net.command;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.TextMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.Session;

import java.net.Socket;

/**
 * Created by ivan on 06.07.17.
 */
public class ChatListCommand extends Command {

    public void execute(Message msg, Session session ) {
        User user = session .getUser();
        Socket clntSock = session .getClntSock();
        String chats = chatStore.getChats(user.getId());
        TextMessage sendTextMessage = new TextMessage();
        sendTextMessage.setType(Type.MSG_TEXT);
        sendTextMessage.setText(chats);
        try {
            sendMessage(sendTextMessage, clntSock);
        } catch (Exception e) {

        }
    }
}
