package receiver;

import msg.Message;

public interface Receiver {
	public boolean receive(Message message);
}
