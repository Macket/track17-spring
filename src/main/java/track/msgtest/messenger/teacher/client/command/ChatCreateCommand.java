package track.msgtest.messenger.teacher.client.command;

import track.msgtest.messenger.messages.ChatCreateMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ivan on 06.07.17.
 */
public class ChatCreateCommand extends Command {

    public void execute(String name, OutputStream out) throws IOException, ProtocolException {
        ChatCreateMessage chatCreateMessage = new ChatCreateMessage(name);
        chatCreateMessage.setType(Type.MSG_CHAT_CREATE);
        sendMessage(chatCreateMessage, out);
    }
}
