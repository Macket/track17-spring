package track.msgtest.messenger.mymessenger;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

/**
 * Created by ivan on 22.04.17.
 */
public class OutThread extends Thread {

    int recvMsgSize;
    byte[] recieveBuf;
    LinkedList<OutputStream> outs;

    public OutThread( int recvMsgSize, byte[] recieveBuf, LinkedList<OutputStream> outs ) {
        this.recvMsgSize = recvMsgSize;
        this.recieveBuf = recieveBuf;
        this.outs = outs;
    }

    @Override
    public void run() {
        try {
            for (OutputStream out : outs) {
                out.write(recieveBuf, 0, recvMsgSize );
                out.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
