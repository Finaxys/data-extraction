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
 * The Interface FXRateProvider.
 */
public interface FXRateProvider {
	
	/**
	 * Gets the current fx rates.
	 *
	 * @param format the format
	 * @param type the type
	 * @return the current fx rates
	 * @throws Exception the exception
	 */
	public List<Document> getCurrentFXRates(ContentType format, DataType type) throws Exception;
}
