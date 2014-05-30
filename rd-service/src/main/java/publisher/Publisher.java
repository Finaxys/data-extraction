package publisher;

import converter.Converter;
import msg.Message;

public interface Publisher {
	public void publish(Message message, Converter converter) throws Exception;
}
