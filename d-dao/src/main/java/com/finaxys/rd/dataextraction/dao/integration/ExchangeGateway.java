package com.finaxys.rd.dataextraction.dao.integration;

import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;

public interface ExchangeGateway {
	/**
	* Gets the exchanges.
	*
	* @param format the format
	* @return the exchanges
	* @throws Exception the exception
	*/
	public Document getExchanges() throws Exception;
}
