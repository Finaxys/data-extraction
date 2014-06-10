/*
 * 
 */
package com.finaxys.rd.dataextraction.provider;

import java.util.List;

import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;

// TODO: Auto-generated Javadoc
/**
 * The Interface StockProvider.
 */
public interface StockProvider {
	
	/**
	 * Gets the stocks.
	 *
	 * @param format the format
	 * @return the stocks
	 * @throws Exception the exception
	 */
	public List<Document> getStocks(ContentType format) throws Exception;
}
