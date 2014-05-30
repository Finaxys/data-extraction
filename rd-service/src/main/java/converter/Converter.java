package converter;

import msg.Message;

public interface Converter {
	public void convert(Message message) throws Exception;
}
