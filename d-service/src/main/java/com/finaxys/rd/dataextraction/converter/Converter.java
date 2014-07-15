/*
 * 
 */
package com.finaxys.rd.dataextraction.converter;

import com.finaxys.rd.dataextraction.domain.msg.Message;

// TODO: Auto-generated Javadoc
/**
 * The Interface Converter.
 */
public interface Converter {
	
	/**
	 * Convert.
	 *
	 * @param message the message
	 * @throws Exception the exception
	 */
	public void convert(Message message) throws Exception;
}
