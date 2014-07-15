package com.finaxys.rd.dataextraction.dao.integration;

import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;

public interface CurrencyPairGateway {
	/**
	* Gets the currency pairs.
	*
	* @param format the format
	* @return the currency pairs
	* @throws Exception the exception
	*/
	public Document getCurrencyPairs() throws Exception;	
}
