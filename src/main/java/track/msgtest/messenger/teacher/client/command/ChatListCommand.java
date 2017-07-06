package track.msgtest.messenger.teacher.client.command;

import track.msgtest.messenger.messages.ChatListMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ivan on 07.07.17.
 */
public class ChatListCommand extends Command {

    public void execute(String sometext, OutputStream out) throws IOException, ProtocolException {
        ChatListMessage chatListMessage = new ChatListMessage();
        chatListMessage.setType(Type.MSG_CHAT_LIST);
        sendMessage(chatListMessage, out);
    }
}
