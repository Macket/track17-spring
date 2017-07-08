package track.msgtest.messenger.messages;

/**
 * Created by ivan on 08.07.17.
 */
public class LoginResultMessage extends Message {
    Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
