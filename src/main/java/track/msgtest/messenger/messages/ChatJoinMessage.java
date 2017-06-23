package track.msgtest.messenger.messages;

/**
 * Created by ivan on 23.06.17.
 */
public class ChatJoinMessage extends Message {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatJoinMessage(String name) {
        this.name = name;
    }
}
