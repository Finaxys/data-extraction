package com.finaxys.rd.dataextraction.dao.integration;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.rd.dataextraction.dao.exception.GatewayException;
import com.finaxys.rd.dataextraction.domain.Option;
import com.finaxys.rd.dataextraction.domain.OptionChain;
import com.finaxys.rd.dataextraction.domain.OptionQuote;

public interface EODOptionQuoteGateway extends EODDataGateway<OptionQuote, Option>{
	
	 List<OptionQuote> getEODData(List<OptionChain> optionChains, LocalDate expiration) throws GatewayException;
	

}
