package com.finaxys.rd.dataextraction.dao.integration;

import org.joda.time.LocalDate;

import com.finaxys.rd.dataextraction.domain.msg.Document;

public interface OptionQuoteGateway {
	public Document getCurrentOptionsQuotes(String symbols, LocalDate expiration) throws Exception;
}
