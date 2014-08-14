package com.finaxys.rd.dataextraction.service;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.rd.dataextraction.domain.Option;
import com.finaxys.rd.dataextraction.domain.OptionChain;
import com.finaxys.rd.dataextraction.domain.OptionQuote;
import com.finaxys.rd.dataextraction.service.exception.ServiceException;

public interface EODOptionQuoteService extends EODDataService<OptionQuote, Option>{
	
	 List<OptionQuote> getEODData(List<OptionChain> optionChains, LocalDate expiration) throws ServiceException;
	

}
