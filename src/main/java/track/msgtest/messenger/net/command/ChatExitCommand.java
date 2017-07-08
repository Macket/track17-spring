package track.msgtest.messenger.net.command;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.CurrentChatMessage;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.Session;

import java.net.Socket;

/**
 * Created by ivan on 06.07.17.
 */
public class ChatExitCommand extends Command {

    public void execute(Message msg, Session session) {
        session.getUser().setCurrentChatId(-1);

        CurrentChatMessage currentChatMessage = new CurrentChatMessage();
        currentChatMessage.setChatName("none");
        currentChatMessage.setType(Type.MSG_CURRENT_CHAT);
        try {
            sendMessage(currentChatMessage, session.getClntSock());
        } catch (Exception ex) {
            System.out.println("Failed to send message to " + session.getClntSock());
        }
    }
}
