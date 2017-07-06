package track.msgtest.messenger.net.command;

import track.msgtest.messenger.messages.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivan on 06.07.17.
 */
public class Commands {
    private Map<Type, Command> commands = new HashMap<>();

    public Commands() {
        commands.put(Type.MSG_TEXT, new TextCommand());
        commands.put(Type.MSG_LOGIN, new LoginCommand());
        commands.put(Type.MSG_CHAT_CREATE, new ChatCreateCommand());
        commands.put(Type.MSG_CHAT_JOIN, new ChatJoinCommand());
        commands.put(Type.MSG_CHAT_EXIT, new ChatExitCommand());
        commands.put(Type.MSG_CHAT_HIST, new ChatHistCommand());
        commands.put(Type.MSG_CHAT_LIST, new ChatListCommand());
    }

    public Command getCommand(Type type) {
        return commands.get(type);
    }
}
