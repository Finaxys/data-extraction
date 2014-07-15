package com.finaxys.rd.dataextraction.dao.integration;

import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataType;

public interface IndexQuoteGateway {

/**
* Gets the current index quotes.
*
* @param format the format
* @param type the type
* @return the current index quotes
* @throws Exception the exception
*/
public Document getCurrentIndexQuotes(String symbols) throws Exception;
}