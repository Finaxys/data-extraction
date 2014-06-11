/*
 * 
 */
package com.finaxys.rd.dataextraction.provider;

import java.util.List;

import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataType;

// TODO: Auto-generated Javadoc
/**
 * The Interface IndexQuoteProvider.
 */
public interface IndexQuoteProvider {
	
	/**
	 * Gets the current index quotes.
	 *
	 * @param format the format
	 * @param type the type
	 * @return the current index quotes
	 * @throws Exception the exception
	 */
	public List<Document> getIndexQuotes(ContentType format, DataType type) throws Exception;
}
