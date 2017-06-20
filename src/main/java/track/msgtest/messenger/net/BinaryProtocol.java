package track.msgtest.messenger.net;

import track.msgtest.messenger.messages.Message;

import java.io.*;

/**
 * Created by ivan on 11.05.17.
 */
public class BinaryProtocol implements Protocol {
    @Override
    public byte[] encode(Message msg) throws ProtocolException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(msg);
            out.flush();
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Message decode(byte[] bytes) throws ProtocolException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        Object obj;
        try {
            in = new ObjectInputStream(bis);
            obj = in.readObject();
            if (obj instanceof Message) {
                return (Message) obj;
            } else {
                throw new ProtocolException("Invalid data");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
