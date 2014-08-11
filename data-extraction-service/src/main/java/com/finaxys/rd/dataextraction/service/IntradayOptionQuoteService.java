package com.finaxys.rd.dataextraction.service;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.rd.dataextraction.domain.Option;
import com.finaxys.rd.dataextraction.domain.OptionChain;
import com.finaxys.rd.dataextraction.domain.OptionQuote;

public interface IntradayOptionQuoteService extends IntradayDataService<OptionQuote, Option>{
	
	public List<OptionQuote> getCurrentData(List<OptionChain> optionChains, LocalDate expiration) throws Exception;
	

}
