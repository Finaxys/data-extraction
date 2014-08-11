/*
 * 
 */
package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.rd.dataextraction.dao.integration.EODOptionQuoteGateway;
import com.finaxys.rd.dataextraction.domain.Option;
import com.finaxys.rd.dataextraction.domain.OptionChain;
import com.finaxys.rd.dataextraction.domain.OptionQuote;
import com.finaxys.rd.dataextraction.service.EODOptionQuoteService;

// TODO: Auto-generated Javadoc
/**
 * The Class EODOptionQuoteServiceImpl.
 */
public class EODOptionQuoteServiceImpl implements EODOptionQuoteService {

	/** The gateway. */
	private EODOptionQuoteGateway gateway;

	public EODOptionQuoteServiceImpl(EODOptionQuoteGateway gateway) {
		super();
		this.gateway = gateway;
	}

	@Override
	public List<OptionQuote> getEODData(List<Option> products) throws Exception {
		try {
			return this.gateway.getEODData(products);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<OptionQuote> getEODData(List<OptionChain> optionChains,
			LocalDate expiration) throws Exception {
		try {
			return this.gateway.getEODData(optionChains, expiration);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}