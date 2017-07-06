package track.msgtest.messenger.teacher.client.command;

import track.msgtest.messenger.messages.ChatExitMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ivan on 07.07.17.
 */
public class ChatExitCommand extends Command {

    public void execute(String sometext, OutputStream out) throws IOException, ProtocolException {
        ChatExitMessage chatExitMessage = new ChatExitMessage();
        chatExitMessage.setType(Type.MSG_CHAT_EXIT);
        sendMessage(chatExitMessage, out);
    }
}
