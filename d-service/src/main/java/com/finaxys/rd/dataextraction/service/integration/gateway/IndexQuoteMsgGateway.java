package com.finaxys.rd.dataextraction.service.integration.gateway;

public interface IndexQuoteMsgGateway {
	/**
* Publish index quotes.
*/
public void publishCurrentIndexQuotesList(String symbols);
}
