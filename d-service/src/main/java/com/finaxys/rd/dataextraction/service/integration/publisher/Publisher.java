/*
 * 
 */
package com.finaxys.rd.dataextraction.service.integration.publisher;

import java.util.List;

import com.finaxys.rd.dataextraction.domain.msg.Message;

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
	public void publish(List<Message> messages) throws Exception;
}
