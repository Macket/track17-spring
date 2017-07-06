package track.msgtest.messenger.teacher.client.command;

import track.msgtest.messenger.messages.TextMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ivan on 07.07.17.
 */
public class TextCommand extends Command {

    public void execute(String text, OutputStream out) throws IOException, ProtocolException {
        TextMessage sendTextMessage = new TextMessage();
        sendTextMessage.setType(Type.MSG_TEXT);
        sendTextMessage.setText(text);
        sendMessage(sendTextMessage, out);
    }
}
