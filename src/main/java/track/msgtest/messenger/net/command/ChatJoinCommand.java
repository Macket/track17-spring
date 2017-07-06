package track.msgtest.messenger.net.command;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.ChatJoinMessage;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.net.InThread;

import java.net.Socket;

/**
 * Created by ivan on 06.07.17.
 */
public class ChatJoinCommand extends Command {

    public void execute(Message msg, InThread inThread) {
        User user = inThread.getUser();
        ChatJoinMessage chatJoinMessage = (ChatJoinMessage) msg;
        user.setCurrentChatId(chatStore.joinChat(user.getId(), chatJoinMessage.getName()));
    }
}
