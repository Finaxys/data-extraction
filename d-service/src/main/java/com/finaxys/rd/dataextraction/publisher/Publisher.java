/*
 * 
 */
package com.finaxys.rd.dataextraction.publisher;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.msg.Message;

// TODO: Auto-generated Javadoc
/**
 * The Interface Publisher.
 */
public interface Publisher {
	
	/**
	 * Publish.
	 *
	 * @param message the message
	 * @throws Exception the exception
	 */
	public void publish(Message message) throws Exception;
}
