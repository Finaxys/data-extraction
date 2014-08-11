/*
 * 
 */
package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.rd.dataextraction.dao.integration.HistOptionQuoteGateway;
import com.finaxys.rd.dataextraction.domain.Option;
import com.finaxys.rd.dataextraction.domain.OptionChain;
import com.finaxys.rd.dataextraction.domain.OptionQuote;
import com.finaxys.rd.dataextraction.service.HistOptionQuoteService;

// TODO: Auto-generated Javadoc
/**
 * The Class HistOptionQuoteServiceImpl.
 */
public class HistOptionQuoteServiceImpl implements HistOptionQuoteService {

	/** The gateway. */
	private HistOptionQuoteGateway gateway;

	public HistOptionQuoteServiceImpl(HistOptionQuoteGateway gateway) {
		super();
		this.gateway = gateway;
	}

	@Override
	public List<OptionQuote> getHistData(List<Option> products,
			LocalDate startDate, LocalDate endDate) throws Exception {
		try {
			return this.gateway.getHistData(products, startDate, endDate);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<OptionQuote> getHistData(List<OptionChain> optionChains,
			LocalDate expiration, LocalDate startDate, LocalDate endDate)
			throws Exception {

		try {
			return this.gateway.getHistData(optionChains, expiration,
					startDate, endDate);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}