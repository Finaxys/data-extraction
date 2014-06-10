/*
 * 
 */
package com.finaxys.rd.dataextraction.provider;

import java.util.List;

import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;

// TODO: Auto-generated Javadoc
/**
 * The Interface CurrencyPairProvider.
 */
public interface CurrencyPairProvider {
	
	/**
	 * Gets the currency pairs.
	 *
	 * @param format the format
	 * @return the currency pairs
	 * @throws Exception the exception
	 */
	public List<Document> getCurrencyPairs(ContentType format) throws Exception;
}
