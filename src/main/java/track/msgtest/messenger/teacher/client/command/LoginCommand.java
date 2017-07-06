package track.msgtest.messenger.teacher.client.command;

import track.msgtest.messenger.messages.ChatListMessage;
import track.msgtest.messenger.messages.LoginMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.ProtocolException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ivan on 07.07.17.
 */
public class LoginCommand extends Command {

    public void execute(String loginAndPass, OutputStream out) throws IOException, ProtocolException {
        String[] nameAndPass = loginAndPass.split(" ");
        if (nameAndPass.length != 2) {
            System.out.println("Ошибка входа");
        } else {
            LoginMessage sendLoginMessage = new LoginMessage(nameAndPass[0], nameAndPass[1]);
            sendMessage(sendLoginMessage, out);
        }
    }
}
