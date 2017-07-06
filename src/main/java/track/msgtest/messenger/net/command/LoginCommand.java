package track.msgtest.messenger.net.command;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.LoginMessage;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.net.InThread;

import java.net.Socket;

/**
 * Created by ivan on 06.07.17.
 */
public class LoginCommand extends Command {


    public void execute(Message msg, InThread inThread) {
        Socket clntSock = inThread.getClntSock();
        LoginMessage loginMessage = (LoginMessage) msg;
        User user = userStore.getUser(loginMessage.getName(), loginMessage.getPass());
        if (user == null) {
            user = userStore.addUser(loginMessage.getName(), loginMessage.getPass());
        }
        if (user == null) {
            System.out.println("Ошибка входа");
        } else {
            clntSocketMap.putIfAbsent(user, clntSock);
            inThread.setUser(user);
        }
    }
}
