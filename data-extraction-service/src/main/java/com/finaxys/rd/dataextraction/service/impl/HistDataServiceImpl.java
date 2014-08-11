/*
 * 
 */
package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.rd.dataextraction.dao.integration.HistDataGateway;
import com.finaxys.rd.dataextraction.domain.MarketData;
import com.finaxys.rd.dataextraction.service.HistDataService;

// TODO: Auto-generated Javadoc
/**
 * The Class MarketDataServiceImpl.
 */
public class HistDataServiceImpl<T extends MarketData, K extends MarketData> implements
		HistDataService<T, K> {

	/** The gateway. */
	private HistDataGateway<T, K> gateway;

	public HistDataServiceImpl(HistDataGateway<T, K> gateway) {
		super();
		this.gateway = gateway;
	}

	@Override
	public List<T> getHistData(List<K> products,
			LocalDate startDate, LocalDate endDate) throws Exception {
		try {
			return this.gateway.getHistData(products, startDate, endDate);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
