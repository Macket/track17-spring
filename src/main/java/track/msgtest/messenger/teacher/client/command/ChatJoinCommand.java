package track.msgtest.messenger.teacher.client.command;

import track.msgtest.messenger.messages.ChatJoinMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ivan on 07.07.17.
 */
public class ChatJoinCommand extends Command {

    public void execute(String name, OutputStream out) throws IOException, ProtocolException {
        ChatJoinMessage chatJoinMessage = new ChatJoinMessage(name);
        chatJoinMessage.setType(Type.MSG_CHAT_JOIN);
        chatJoinMessage.setName(name);
        sendMessage(chatJoinMessage, out);
    }
}
