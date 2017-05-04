package track.msgtest.messenger.mymessenger;

/**
 * Created by ivan on 26.04.17.
 */
public class Message {
    private byte[] recieveBuf;
    private int recvMsgSize;

    public Message(int recvMsgSize, byte[] recieveBuf) {
        this.recieveBuf = recieveBuf;
        this.recvMsgSize = recvMsgSize;
    }

    int getRecvMsgSize() {
        return recvMsgSize;
    }

    byte[] getRecieveBuf() {
        return recieveBuf;
    }
}
