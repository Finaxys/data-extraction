package com.finaxys.rd.dataextraction.service.integration.gateway;

public interface StockQuoteMsgGateway {

/**
* Publish stocks quotes.
*/
public void publishCurrentStocksQuotesList(String symbols);
}
