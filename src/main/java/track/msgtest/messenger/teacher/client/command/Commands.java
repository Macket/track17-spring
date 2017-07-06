package track.msgtest.messenger.teacher.client.command;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivan on 07.07.17.
 */
public class Commands {
    private Map<String, Command> commands = new HashMap<>();

    public Commands() {
        commands.put("/text", new TextCommand());
        commands.put("/login", new LoginCommand());
        commands.put("/chat_create", new ChatCreateCommand());
        commands.put("/chat_join", new ChatJoinCommand());
        commands.put("/chat_exit", new ChatExitCommand());
        commands.put("/chat_hist", new ChatHistCommand());
        commands.put("/chat_list", new ChatListCommand());
    }

    public Command getCommand(String token) {
        return commands.get(token);
    }
}
