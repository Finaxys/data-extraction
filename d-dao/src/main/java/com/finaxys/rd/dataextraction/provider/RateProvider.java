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
public interface RateProvider {
	/**
	 * Gets the rates.
	 *
	 * @param format the format
	 * @return the rates
	 * @throws Exception the exception
	 */
	public List<Document> getRates(ContentType format) throws Exception;
}
