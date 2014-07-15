package com.finaxys.rd.dataextraction.dao.integration;

import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;

public interface StockGateway {
	/**
* Gets the stocks.
*
* @param format the format
* @return the stocks
* @throws Exception the exception
*/
public Document getStocks() throws Exception;
}