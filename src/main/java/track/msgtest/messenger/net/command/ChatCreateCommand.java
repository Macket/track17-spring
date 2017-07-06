package track.msgtest.messenger.net.command;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.ChatCreateMessage;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.net.InThread;

import java.net.Socket;

/**
 * Created by ivan on 06.07.17.
 */
public class ChatCreateCommand extends Command {

    public void execute(Message msg, InThread inThread) {
        User user = inThread.getUser();
        ChatCreateMessage chatCreateMessage = (ChatCreateMessage) msg;
        chatCreateMessage.setSenderId(user.getId());
        long chatId = chatStore.createChat(chatCreateMessage);
        user.setCurrentChatId(chatId);
    }
}
