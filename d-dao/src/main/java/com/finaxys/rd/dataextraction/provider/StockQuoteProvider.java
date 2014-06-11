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
 * The Interface StockQuoteProvider.
 */
public interface StockQuoteProvider {
	
	/**
	 * Gets the current stocks quotes.
	 *
	 * @param format the format
	 * @param types the types
	 * @return the current stocks quotes
	 * @throws Exception the exception
	 */
	public List<Document> getStocksQuotes(ContentType format, DataType types) throws Exception;
}
