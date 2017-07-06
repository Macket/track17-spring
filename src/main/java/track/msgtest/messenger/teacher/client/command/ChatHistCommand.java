package track.msgtest.messenger.teacher.client.command;

import track.msgtest.messenger.messages.ChatHistMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ivan on 07.07.17.
 */
public class ChatHistCommand extends Command {

    public void execute(String sometext, OutputStream out) throws IOException, ProtocolException {
        ChatHistMessage chatHistMessage = new ChatHistMessage();
        chatHistMessage.setType(Type.MSG_CHAT_HIST);
        sendMessage(chatHistMessage, out);
    }
}
