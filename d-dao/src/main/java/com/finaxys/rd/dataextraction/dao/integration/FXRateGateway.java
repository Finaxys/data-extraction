package com.finaxys.rd.dataextraction.dao.integration;

import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataType;

public interface FXRateGateway {
	/**
* Gets the current fx rates.
*
* @param format the format
* @param type the type
* @return the current fx rates
* @throws Exception the exception
*/
public Document getCurrentFXRates(String symbols) throws Exception;
}