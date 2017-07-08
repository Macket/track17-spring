package track.msgtest.messenger.messages;

/**
 * Created by ivan on 08.07.17.
 */
public class CurrentChatMessage extends Message {
    String chatName;

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }
}
