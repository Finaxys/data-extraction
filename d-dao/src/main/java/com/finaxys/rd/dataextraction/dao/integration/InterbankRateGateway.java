package com.finaxys.rd.dataextraction.dao.integration;

import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;

public interface InterbankRateGateway {
	/**
* Gets the rates.
*
* @param format the format
* @return the rates
* @throws Exception the exception
*/
public Document getRates() throws Exception;
}