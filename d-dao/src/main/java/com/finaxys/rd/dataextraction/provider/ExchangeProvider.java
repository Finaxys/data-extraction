/*
 * 
 */
package com.finaxys.rd.dataextraction.provider;

import java.util.List;

import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;

// TODO: Auto-generated Javadoc
/**
 * The Interface ExchangeProvider.
 */
public interface ExchangeProvider {
	
	/**
	 * Gets the exchanges.
	 *
	 * @param format the format
	 * @return the exchanges
	 * @throws Exception the exception
	 */
	public List<Document> getExchanges(ContentType format) throws Exception;
}
