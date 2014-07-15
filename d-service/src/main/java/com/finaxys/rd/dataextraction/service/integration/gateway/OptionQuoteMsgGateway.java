package com.finaxys.rd.dataextraction.service.integration.gateway;

import org.joda.time.LocalDate;

public interface OptionQuoteMsgGateway {

/**
* Publish stocks quotes.
*/
public void publishCurrentOptionsQuotesList(String symbols, LocalDate expiration);
}
