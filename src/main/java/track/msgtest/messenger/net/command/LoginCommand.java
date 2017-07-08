package track.msgtest.messenger.net.command;

import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.*;
import track.msgtest.messenger.net.Session;

import java.net.Socket;

/**
 * Created by ivan on 06.07.17.
 */
public class LoginCommand extends Command {


    public void execute(Message msg, Session session ) {
        Socket clntSock = session .getClntSock();
        LoginMessage loginMessage = (LoginMessage) msg;
        User user = userStore.getUser(loginMessage.getName(), loginMessage.getPass());
        if (user == null) {
            user = userStore.addUser(loginMessage.getName(), loginMessage.getPass());
        }
        if (user == null) {
            LoginResultMessage loginResultMessage = new LoginResultMessage();
            loginResultMessage.setStatus(Status.ERROR);
            try {
                sendMessage(loginResultMessage, session .getClntSock());
            } catch (Exception ex) {
                System.out.println("Failed to send message to " + session .getClntSock());
            }
        } else {
            clntSocketMap.putIfAbsent(user, clntSock);
            session .setUser(user);

            LoginResultMessage loginResultMessage = new LoginResultMessage();
            loginResultMessage.setStatus(Status.OK);
            loginResultMessage.setSenderName(user.getName());
            loginResultMessage.setType(Type.MSG_LOGIN_RESULT);
            try {
                sendMessage(loginResultMessage, session .getClntSock());
            } catch (Exception ex) {
                System.out.println("Failed to send message to " + session .getClntSock());
            }
        }
    }
}
