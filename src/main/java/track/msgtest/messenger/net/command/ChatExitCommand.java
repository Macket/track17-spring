package track.msgtest.messenger.net.command;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.net.InThread;

import java.net.Socket;

/**
 * Created by ivan on 06.07.17.
 */
public class ChatExitCommand extends Command {

    public void execute(Message msg, InThread inThread) {
        inThread.getUser().setCurrentChatId(-1);
    }
}
