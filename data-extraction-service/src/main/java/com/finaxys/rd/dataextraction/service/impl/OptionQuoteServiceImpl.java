/*
 * 
 */
package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.rd.dataextraction.dao.integration.IntradayOptionQuoteGateway;
import com.finaxys.rd.dataextraction.domain.Option;
import com.finaxys.rd.dataextraction.domain.OptionChain;
import com.finaxys.rd.dataextraction.domain.OptionQuote;
import com.finaxys.rd.dataextraction.service.IntradayOptionQuoteService;

// TODO: Auto-generated Javadoc
/**
 * The Class OptionQuoteServiceImpl.
 */
public class OptionQuoteServiceImpl implements IntradayOptionQuoteService {

	/** The gateway. */
	private IntradayOptionQuoteGateway gateway;

	public OptionQuoteServiceImpl(IntradayOptionQuoteGateway gateway) {
		super();
		this.gateway = gateway;
	}

	@Override
	public List<OptionQuote> getCurrentData(List<Option> products)
			throws Exception {
		try {
			return this.gateway.getCurrentData(products);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<OptionQuote> getCurrentData(List<OptionChain> optionChains,
			LocalDate expiration) throws Exception {
		try {
			return this.gateway.getCurrentData(optionChains, expiration);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}