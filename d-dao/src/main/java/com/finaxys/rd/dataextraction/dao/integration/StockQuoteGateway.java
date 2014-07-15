package com.finaxys.rd.dataextraction.dao.integration;

import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataType;

public interface StockQuoteGateway {
	/**
* Gets the current stocks quotes.
*
* @param format the format
* @param types the types
* @return the current stocks quotes
* @throws Exception the exception
*/
public Document getCurrentStocksQuotes(String symbols) throws Exception;
}